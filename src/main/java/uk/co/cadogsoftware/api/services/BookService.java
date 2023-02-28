package uk.co.cadogsoftware.api.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.co.cadogsoftware.api.database.repositories.BookRepository;
import uk.co.cadogsoftware.api.dtos.BookDTO;
import uk.co.cadogsoftware.api.exceptions.BookAlreadyExistsException;
import uk.co.cadogsoftware.api.exceptions.BookIdAlreadyInUseException;
import uk.co.cadogsoftware.api.exceptions.BookNotFoundException;

/**
 * A class that controls accessing books.
 */
@Service
@RequiredArgsConstructor
public class BookService {

  private final BookRepository bookRepository;

  public BookDTO getBook(Long id) {
    return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
  }

  public List<BookDTO> getBooks(String titleFilter) {

    if (StringUtils.hasText(titleFilter)) {
      return getBookByTitle(titleFilter);
    } else {
      return getAllBooks();
    }

  }

  private List<BookDTO> getAllBooks() {
    return bookRepository.findAll();
  }

  private List<BookDTO> getBookByTitle(String titleFilter) {
    return getAllBooks().stream().filter(bookDTO -> bookDTO.getTitle().contains(titleFilter.trim()))
        .toList();
    // If no books are found just return an empty list.
  }

  public BookDTO addBook(BookDTO bookDTO) {
    if (isIdInUse(bookDTO.getId())) {
      throw new BookIdAlreadyInUseException(bookDTO);
    }

    if (doesBookExistByTitleAndAuthor(bookDTO)) {
      throw new BookAlreadyExistsException(bookDTO);
    }

    bookRepository.save(bookDTO);
    return bookDTO;
  }

  private boolean isIdInUse(Long id) {
    return bookRepository.findById(id).isPresent();
  }

  private boolean doesBookExistByTitleAndAuthor(BookDTO bookDTO) {

    // TODO: use repository.findBy here.
    List<BookDTO> allMatchingBooksByTitleAndAuthor =
        getAllBooks().stream()
            .filter(book -> book.getTitle().equals(bookDTO.getTitle().trim()))
            .filter(book -> book.getAuthor().equals(bookDTO.getAuthor().trim()))
            .toList();

    return !allMatchingBooksByTitleAndAuthor.isEmpty();

  }

  //
  public void removeBook(Long bookId) {
    bookRepository.deleteById(bookId);
  }

}
