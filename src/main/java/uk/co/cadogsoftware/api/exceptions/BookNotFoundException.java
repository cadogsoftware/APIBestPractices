package uk.co.cadogsoftware.api.exceptions;

public class BookNotFoundException extends RuntimeException {

  public BookNotFoundException(int id) {
    super("Book cannot be found for id: " + id);
  }

}
