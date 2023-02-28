package uk.co.cadogsoftware.api.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.co.cadogsoftware.api.dtos.BookDTO;
import uk.co.cadogsoftware.api.services.BookService;

/**
 * Handles REST requests for {@link BookDTO}s.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class BookController {

  private final BookService bookService;

  /**
   * Get all of the {@link BookDTO}s.
   * <p>
   * Note use of the {@link RequestParam} annotation here. This is set to required = false so that
   * we can return all books or books with the given title.
   * </p>
   * <p>
   * The link {@link RequestParam} annotation is not used to identify the resource (a book), but it
   * is used for filtering of the results. If I wanted to identify the resource I would use a
   * {@link PathVariable} instead, as in the other method in this class that gets a single
   * {@link BookDTO}. Another use of {@link RequestParam} would be for sorting of the results.
   * </p>
   *
   * @param title - an optional title to filter the results by.
   * @return - all books or books with the given title.
   */
  // Aggregate root
  // tag::get-aggregate-root[]
  @GetMapping("/books")
  public List<BookDTO> getBooks(@RequestParam(required = false) String title) {
    log.debug("Getting all books");
    return bookService.getBooks(title);
  }
  // end::get-aggregate-root[]

  /**
   * Gets a single book. Note that as we are getting a resource we use {@link PathVariable}, not
   * {@link RequestParam}, which should generally be used for sorting or filtering the results.
   *
   * @param id - the id of the Book.
   * @return - the Book with the given id.
   */
  @GetMapping("/books/{id}")
  public BookDTO getOneBook(@PathVariable(value = "id") Long id) {
    return bookService.getBook(id);
  }

  @PostMapping("/books")
  public BookDTO addBook(@RequestBody BookDTO bookDTO) {
    return bookService.addBook(bookDTO);
  }

  @DeleteMapping("books/{id}")
  public void deleteBook(@PathVariable Long id) {
    bookService.removeBook(id);
  }

}
