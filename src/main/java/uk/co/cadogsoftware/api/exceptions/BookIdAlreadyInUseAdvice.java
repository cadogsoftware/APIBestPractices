package uk.co.cadogsoftware.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BookIdAlreadyInUseAdvice {

  @ResponseBody
  @ExceptionHandler(BookIdAlreadyInUseException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  String bookIdAlreadyInUseHandler(BookIdAlreadyInUseException ex) {
    return ex.getMessage();
  }

}
