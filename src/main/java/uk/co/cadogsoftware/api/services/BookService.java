package uk.co.cadogsoftware.api.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 *
 * <p>
 * Handles the conversion of {@link BookDTO}s to {@link Book}s and vice versa with use of the
 * {@link BookConverter}.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
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
    Book book = bookRepository.findByIsbn(isbn);
    if (book != null) {
      bookRepository.deleteById(book.getId());
    } else {
      log.warn("Book requested for deletion but was not found for ISBN: {}", isbn);
    }
  }

  public BookDTO addBook(BookDTO bookDto) {

    if (doesBookExistByIsbn(bookDto)) {
      throw new BookAlreadyExistsException("Book already exists for ISBN: " + bookDto.getIsbn());
    }

    if (doesBookExistByTitleAndAuthor(bookDto)) {
      throw new BookAlreadyExistsException(
          "Book already exists with title: " + bookDto.getTitle() + " and author: "
              + bookDto.getAuthorFirstName() + " " + bookDto.getAuthorLastName());
    }

    Book book = bookConverter.convertToBook(bookDto);

    bookRepository.save(book);
    return bookDto;
  }

  private boolean doesBookExistByTitleAndAuthor(BookDTO bookDtoToLookFor) {
    List<Book> allMatchingBooksByTitleAndAuthor =
        bookRepository.findByTitleAndAuthorFirstNameAndAuthorLastName(
        bookDtoToLookFor.getTitle(), bookDtoToLookFor.getAuthorFirstName(),
        bookDtoToLookFor.getAuthorLastName());
    return !allMatchingBooksByTitleAndAuthor.isEmpty();
  }

  private boolean doesBookExistByIsbn(BookDTO bookDTO) {
    Book allMatchingBooksByIsbn = bookRepository.findByIsbn(bookDTO.getIsbn());
    return allMatchingBooksByIsbn != null;
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
