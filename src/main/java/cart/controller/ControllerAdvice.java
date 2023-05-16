package cart.controller;

import java.sql.SQLDataException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cart.exception.DomainException;
import cart.exception.ExceptionCode;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ExceptionCode> handleDomainException(DomainException e) {
        return ResponseEntity.badRequest().body(e.getExceptionCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionCode> handleBadRequest(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(ExceptionCode.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionCode> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(ExceptionCode.ILLEGAL_ARGUMENT);
    }

    @ExceptionHandler(SQLDataException.class)
    public ResponseEntity<ExceptionCode> handleDataException(SQLDataException e) {
        return ResponseEntity.badRequest().body(ExceptionCode.NO_DATA);
    }
}
