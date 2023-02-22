package uk.co.cadogsoftware.api.services;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.co.cadogsoftware.api.dtos.BookDTO;
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
  private static final List<BookDTO> ALL_BOOKS = Collections.synchronizedList(List.of(
      new BookDTO(1, "Animal Farm", "George Orwell"),
      new BookDTO(2, "1984", "George Orwell"),
      new BookDTO(3, "Harper Lee", "To Kill a Mockingbird"),
      new BookDTO(3, "Harper Lee", "DUPLICATE ID - To Kill a Mockingbird"),
      // Deliberately set a duplicate id here for now.
      new BookDTO(4, "Farming World", "Tom Smith")));

  public BookDTO getBook(int id) {
    List<BookDTO> bookList = ALL_BOOKS.stream().filter(bookDTO -> bookDTO.id() == id).toList();

    if (bookList.isEmpty()) {
      throw new BookNotFoundException(id); // This is handled by the BookNotFoundAdvice.
    }

    if (bookList.size() > 1) {
      throw new TooManyBooksFoundException(id); // This is handled by the TooManyBooksFoundAdvice.
    }

    return bookList.get(0);
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

}
