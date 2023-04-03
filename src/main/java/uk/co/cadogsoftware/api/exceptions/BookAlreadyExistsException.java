package uk.co.cadogsoftware.api.exceptions;

import uk.co.cadogsoftware.api.dtos.BookDTO;

/**
 * Used to indicate that a book already exists.
 */
public class BookAlreadyExistsException extends RuntimeException {

  public BookAlreadyExistsException(String message) {
    super(message);
  }

}
