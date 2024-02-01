package eu.alehem.longestlines.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BusLineNotFoundAdvice {
  @ResponseBody
  @ExceptionHandler(BusLineNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String busLineNotFoundHandler(BusLineNotFoundException ex) {
    return ex.getMessage();
  }
}
