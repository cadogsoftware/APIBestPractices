package uk.co.cadogsoftware.api.exceptions;

public class TooManyBooksFoundException extends RuntimeException {

  public TooManyBooksFoundException(int id) {
    super("Too many books found for id: " + id);
  }

}
