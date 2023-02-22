package uk.co.cadogsoftware.api.exceptions;

/**
 * Used to indicate that too many books with the id provided have been found.
 */
public class TooManyBooksFoundException extends RuntimeException {

  public TooManyBooksFoundException(int id) {
    super("Too many books found for id: " + id);
  }

}
