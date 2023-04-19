package uk.co.cadogsoftware.api.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.co.cadogsoftware.api.assemblers.BookModelAssembler;
import uk.co.cadogsoftware.api.dtos.BookDTO;
import uk.co.cadogsoftware.api.exceptions.BookNotFoundException;
import uk.co.cadogsoftware.api.services.BookService;

/**
 * Handles REST requests for {@link BookDTO}s.
 *
 * @author Richard Morgan
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class BookController {

  private final BookService bookService;

  private final BookModelAssembler bookModelAssembler;

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
   * @return - all books or books containing the given title.
   */
  @GetMapping("/books")
  public CollectionModel<EntityModel<BookDTO>> getBooks(
      @RequestParam(required = false) String title) {
    log.debug("Getting all books with a title that contains: {} ", title);

    List<BookDTO> books = bookService.findBooks(title);
    if (CollectionUtils.isEmpty(books)) {
      throw new BookNotFoundException("No books found for title " + title);
    }

    List<EntityModel<BookDTO>> booksModels = books.stream()
        .map(bookModelAssembler::toModel)
        .toList();

    return CollectionModel.of(booksModels,
        linkTo(methodOn(BookController.class).getBooks("")).withSelfRel());
  }

  /**
   * Gets a single book. Note that as we are getting a resource we use {@link PathVariable}, not
   * {@link RequestParam}, which should generally be used for sorting or filtering the results.
   *
   * @param isbn - the ISBN of the Book.
   * @return - the Book with the given ISBN.
   */
  @GetMapping("/books/{isbn}")
  public EntityModel<BookDTO> getOneBook(@PathVariable(value = "isbn") String isbn) {
    // TODO: validate input
    return bookModelAssembler.toModel(bookService.getBook(isbn));
  }

  @PostMapping("/books")
  public EntityModel<BookDTO> addBook(@RequestBody BookDTO bookDTO) {
    // TODO: validate input
    return bookModelAssembler.toModel(bookService.addBook(bookDTO));
  }

  @DeleteMapping("books/{isbn}")
  public void deleteBook(@PathVariable String isbn) {
    // TODO: validate isbn here.
    bookService.removeBook(isbn);
  }

}
