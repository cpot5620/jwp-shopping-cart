package cart.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CartException {

  public BadRequestException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
