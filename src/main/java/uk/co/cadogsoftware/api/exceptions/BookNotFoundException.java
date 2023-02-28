package uk.co.cadogsoftware.api.exceptions;

/**
 * Used to indicate that a book with the id provided cannot be found.
 */
public class BookNotFoundException extends RuntimeException {

  public BookNotFoundException(Long id) {
    super("Book cannot be found for id: " + id);
  }

}
