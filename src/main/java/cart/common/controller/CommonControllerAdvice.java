package cart.common.controller;

import cart.common.exception.PersistenceException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CommonControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> sendErrorMessage(final MethodArgumentNotValidException exception) {
        List<ObjectError> errors = exception.getBindingResult()
                .getAllErrors();

        String exceptionMessage = errors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" / "));

        return ResponseEntity
                .badRequest()
                .body(exceptionMessage);
    }

    @ExceptionHandler(PersistenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String sendMessage(PersistenceException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> sendExceptionMessage(RuntimeException exception) {
        exception.printStackTrace();
        String exceptionMessage = "서버 내부의 오류로 인해 작업을 처리하지 못했습니다.";
        return ResponseEntity
                .internalServerError()
                .body(exceptionMessage);
    }
}
