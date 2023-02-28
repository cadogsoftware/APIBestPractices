package uk.co.cadogsoftware.api.exceptions;

import uk.co.cadogsoftware.api.dtos.BookDTO;

/**
 * Used to indicate that a book id is in use.
 */
public class BookIdAlreadyInUseException extends RuntimeException {

  public BookIdAlreadyInUseException(BookDTO bookDTO) {
    super("Book id is already in use: " + bookDTO.getId());
  }

}
