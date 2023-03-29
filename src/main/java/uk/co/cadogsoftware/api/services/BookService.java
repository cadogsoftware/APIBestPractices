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

  public BookDTO getBook(Long id) {
    Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    return bookConverter.convertToBookDTO(book);
  }

  public List<BookDTO> getBooks(String titleFilter) {

    if (StringUtils.hasText(titleFilter)) {
      return getBookByTitle(titleFilter);
    } else {
      return getAllBooks();
    }

  }

  public void removeBook(Long bookId) {
    bookRepository.deleteById(bookId);
  }

  public BookDTO addBook(BookDTO bookDto) {
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
            .filter(bookDTO -> bookDTO.getTitle().equals(bookDtoToLookFor.getTitle().trim()))
            .filter(bookDTO -> bookDTO.getAuthor().equals(bookDtoToLookFor.getAuthor().trim()))
            .toList();

    return !allMatchingBooksByTitleAndAuthor.isEmpty();

  }

  private List<BookDTO> getAllBooks() {
    List<Book> books = bookRepository.findAll();
    return bookConverter.convertToBookDTOList(books);
  }

  private List<BookDTO> getBookByTitle(String titleFilter) {
    return getAllBooks().stream().filter(book -> book.getTitle().contains(titleFilter.trim())).toList();
    // If no books are found just return an empty list.
  }
}
