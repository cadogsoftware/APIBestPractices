package uk.co.cadogsoftware.api.exceptions;

/**
 * Used to indicate that a book with the ISBN provided cannot be found.
 */
public class BookNotFoundException extends RuntimeException {

  public BookNotFoundException(String isbn) {
    super("Book cannot be found for ISBN: " + isbn);
  }

}
