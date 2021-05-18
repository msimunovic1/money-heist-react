package hr.msimunovic.moneyheist.api.exception.handler;

import hr.msimunovic.moneyheist.api.exception.BadRequestException;
import hr.msimunovic.moneyheist.api.exception.MethodNotAllowedException;
import hr.msimunovic.moneyheist.api.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ NotFoundException.class })
    public ResponseEntity<Object> handleNotFound(Exception exception) {
        return this.handleErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());

    }

    @ExceptionHandler({ BadRequestException.class })
    public ResponseEntity<Object> handleBadRequest(Exception exception) {
        return this.handleErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());

    }

    @ExceptionHandler({ MethodNotAllowedException.class })
    public ResponseEntity<Object> handleMethodNotAllowed(Exception exception) {
        return this.handleErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, exception.getMessage());

    }

    public ResponseEntity<Object> handleErrorResponse(HttpStatus status, String errorMessage) {

        GlobalExceptionResponse exception = new GlobalExceptionResponse();
        exception.setTimestamp(LocalDateTime.now());
        exception.setStatus(status.value());
        exception.setMessage(errorMessage);

        return new ResponseEntity<Object>(exception, status);
    }

}