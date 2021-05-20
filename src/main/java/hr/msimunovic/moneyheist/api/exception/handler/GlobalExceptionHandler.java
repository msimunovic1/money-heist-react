package hr.msimunovic.moneyheist.api.exception.handler;

import hr.msimunovic.moneyheist.api.exception.BadRequestException;
import hr.msimunovic.moneyheist.api.exception.MethodNotAllowedException;
import hr.msimunovic.moneyheist.api.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        List<String> errors = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        return handleErrorResponse(status, errors);

    }

    @ExceptionHandler({ NotFoundException.class })
    public ResponseEntity<Object> handleNotFound(Exception exception) {
        List<String> errors = Collections.singletonList(exception.getMessage());
        return this.handleErrorResponse(HttpStatus.NOT_FOUND, errors);

    }

    @ExceptionHandler({ BadRequestException.class })
    public ResponseEntity<Object> handleBadRequest(Exception exception) {
        List<String> errors = Collections.singletonList(exception.getMessage());
        return this.handleErrorResponse(HttpStatus.BAD_REQUEST, errors);

    }

    @ExceptionHandler({ MethodNotAllowedException.class })
    public ResponseEntity<Object> handleMethodNotAllowed(Exception exception) {
        List<String> errors = Collections.singletonList(exception.getMessage());
        return this.handleErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, errors);

    }

    public ResponseEntity<Object> handleErrorResponse(HttpStatus status, List<String> errorMessages) {

        GlobalExceptionResponse exception = new GlobalExceptionResponse();
        exception.setTimestamp(LocalDateTime.now());
        exception.setStatus(status.value());
        exception.setMessage(errorMessages);

        return new ResponseEntity<Object>(exception, status);
    }

}