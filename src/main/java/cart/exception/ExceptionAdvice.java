package cart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorDto>> handleException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final List<ErrorDto> errors = exception.getFieldErrors()
                                               .stream()
                                               .map(fieldError -> String.format("%s %s", fieldError.getField(), fieldError.getDefaultMessage()))
                                               .map(message -> new ErrorDto(httpStatus.value(), message, request.getRequestURI()))
                                               .collect(Collectors.toUnmodifiableList());

        return ResponseEntity.status(httpStatus.value())
                             .body(errors);
    }

    @ExceptionHandler({IllegalArgumentException.class, UnauthorizedMemberException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorDto> handleException(Exception exception, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return handleException(exception, httpStatus, request.getRequestURI());
    }

    @ExceptionHandler({DuplicateEmailException.class, DuplicateCartItemException.class})
    public ResponseEntity<ErrorDto> handleException(DuplicateException exception, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        return handleException(exception, httpStatus, request.getRequestURI());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorDto> handleException(NoSuchElementException exception, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return handleException(exception, httpStatus, request.getRequestURI());
    }

    private ResponseEntity<ErrorDto> handleException(final Exception exception, final HttpStatus httpStatus, final String path) {
        final ErrorDto error = new ErrorDto(httpStatus.value(), exception.getMessage(), path);
        return ResponseEntity.status(httpStatus.value())
                             .body(error);
    }
}
