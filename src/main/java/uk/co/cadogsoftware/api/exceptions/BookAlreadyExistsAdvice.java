package uk.co.cadogsoftware.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception handler for {@link BookAlreadyExistsException}.
 */
@ControllerAdvice
public class BookAlreadyExistsAdvice {

  @ResponseBody
  @ExceptionHandler(BookAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String bookAlreadyExistsHandler(BookAlreadyExistsException ex) {
    return ex.getMessage();
  }

}
