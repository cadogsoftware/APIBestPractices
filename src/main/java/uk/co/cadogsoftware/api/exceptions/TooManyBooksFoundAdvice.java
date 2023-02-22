package uk.co.cadogsoftware.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TooManyBooksFoundAdvice {

  @ResponseBody
  @ExceptionHandler(TooManyBooksFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String tooManyBooksFoundHanlder(TooManyBooksFoundException ex) {
    return ex.getMessage();
  }

}
