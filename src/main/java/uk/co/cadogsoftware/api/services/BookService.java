package uk.co.cadogsoftware.api.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.co.cadogsoftware.api.dtos.BookDTO;
import uk.co.cadogsoftware.api.exceptions.BookAlreadyExistsException;
import uk.co.cadogsoftware.api.exceptions.BookNotFoundException;
import uk.co.cadogsoftware.api.exceptions.TooManyBooksFoundException;

/**
 * A class that controls accessing books.
 */
@Service
public class BookService {

  /*
  Store the list of BookDTOs here until we set up a DB of some kind or add in methods to add
  and remove books.
  Note that although this is only a temporary solution we will
  make sure it is thread safe by using a synchronized list.
  */
  private static final List<BookDTO> ALL_BOOKS = Collections.synchronizedList(new ArrayList<>());

  public BookService() {
    ALL_BOOKS.add(new BookDTO(1, "Animal Farm", "George Orwell"));
    ALL_BOOKS.add(new BookDTO(2, "1984", "George Orwell"));
    ALL_BOOKS.add(new BookDTO(3, "Harper Lee", "To Kill a Mockingbird"));
    ALL_BOOKS.add(new BookDTO(3, "Harper Lee", "DUPLICATE ID - To Kill a Mockingbird"));
    // Deliberately set a duplicate id here for now.
    ALL_BOOKS.add(new BookDTO(4, "Farming World", "Tom Smith"));
  }

  public BookDTO getBook(int id) {
    // NOTE: as we are using a synchronized list then we should sync access to the stream iterator
    // too, but we will get rid of that soon so don't bother.
    List<BookDTO> bookList = ALL_BOOKS.stream().filter(bookDTO -> bookDTO.id() == id).toList();

    if (bookList.isEmpty()) {
      throw new BookNotFoundException(id); // This is handled by the BookNotFoundAdvice.
    }

    if (bookList.size() > 1) {
      throw new TooManyBooksFoundException(id); // This is handled by the TooManyBooksFoundAdvice.
    }

    return bookList.get(0);
  }

  private BookDTO getBookById(int bookId) {
    List<BookDTO> bookList = ALL_BOOKS.stream().filter(bookDTO -> bookDTO.id() == bookId).toList();
    if (!bookList.isEmpty()) {
      // Let's assume book ids are unique here for now
      return bookList.get(0);
    } else {
      return null;
    }
  }

  public List<BookDTO> getBooks(String titleFilter) {
    if (StringUtils.hasText(titleFilter)) {
      return getBookByTitle(titleFilter.trim());
    } else {
      return getAllBooks();
    }

  }

  private List<BookDTO> getAllBooks() {
    return ALL_BOOKS;
  }

  private List<BookDTO> getBookByTitle(String titleFilter) {
    return ALL_BOOKS.stream().filter(bookDTO -> bookDTO.title().contains(titleFilter)).toList();
    // If no books are found just return an empty list.
  }

  public BookDTO addBook(BookDTO bookDTO) {
    if (doesBookAlreadyExist(bookDTO)) {
      throw new BookAlreadyExistsException(bookDTO);
    }
    ALL_BOOKS.add(bookDTO);
    return bookDTO;
  }

  private boolean doesBookAlreadyExist(BookDTO bookDTO) {
    boolean isBookPresent =
        !ALL_BOOKS.stream().filter(book -> book.id() == bookDTO.id()).toList().isEmpty();

    if (!isBookPresent) {
      isBookPresent = !ALL_BOOKS.stream()
          .filter(book -> (book.title().equals(bookDTO.title()))
              && (book.author().equals(bookDTO.author())))
          .toList().isEmpty();
    }

    return isBookPresent;
  }

  public void removeBook(int bookId) {
    BookDTO bookToRemove = getBookById(bookId);
    if (bookToRemove != null) {
      ALL_BOOKS.remove(bookToRemove);
    } // else do nothing.

  }

}
