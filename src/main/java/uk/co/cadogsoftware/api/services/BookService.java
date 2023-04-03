package uk.co.cadogsoftware.api.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.co.cadogsoftware.api.converters.BookConverter;
import uk.co.cadogsoftware.api.database.entities.Book;
import uk.co.cadogsoftware.api.database.repositories.BookRepository;
import uk.co.cadogsoftware.api.dtos.BookDTO;
import uk.co.cadogsoftware.api.exceptions.BookAlreadyExistsException;
import uk.co.cadogsoftware.api.exceptions.BookNotFoundException;

/**
 * A class that controls interactions with books.
 * <p>
 * Handles the conversion of {@link BookDTO}s to {@link Book}s and vice versa with use of the
 * {@link BookConverter}.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class BookService {

  private final BookRepository bookRepository;

  private final BookConverter bookConverter;

  public BookDTO getBook(String isbn) {
    Book book = bookRepository.findByIsbn(isbn);
    if (book == null) {
      throw new BookNotFoundException(isbn);
    }
    return bookConverter.convertToBookDTO(book);
  }

  public List<BookDTO> findBooks(String titleFilter) {

    if (StringUtils.hasText(titleFilter)) {
      return findBooksMatchingTitle(titleFilter);
    } else {
      return getAllBooks();
    }

  }

  public void removeBook(String isbn) {
    bookRepository.deleteByIsbn(isbn);
  }

  public BookDTO addBook(BookDTO bookDto) {

    // TODO: check to see if the ISBN already exists.

    if (doesBookExistByTitleAndAuthor(bookDto)) {
      throw new BookAlreadyExistsException(bookDto);
    }

    Book book = bookConverter.convertToBook(bookDto);

    bookRepository.save(book);
    return bookDto;
  }

  private boolean doesBookExistByTitleAndAuthor(BookDTO bookDtoToLookFor) {

    // TODO: use repository.findBy here.
    List<BookDTO> allMatchingBooksByTitleAndAuthor =
        getAllBooks().stream()
            .filter(bookDTO -> bookDTO.title().equals(bookDtoToLookFor.title().trim()))
            .filter(bookDTO -> bookDTO.author().equals(bookDtoToLookFor.author().trim()))
            .toList();

    return !allMatchingBooksByTitleAndAuthor.isEmpty();

  }

  private List<BookDTO> getAllBooks() {
    List<Book> books = bookRepository.findAll();
    return bookConverter.convertToBookDTOList(books);
  }

  private List<BookDTO> findBooksMatchingTitle(String titleFilter) {
    List<Book> books = bookRepository.findByTitleContaining(titleFilter);
    return bookConverter.convertToBookDTOList(books);
  }
}
