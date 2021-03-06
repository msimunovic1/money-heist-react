package hr.msimunovic.moneyheist.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {

        super(message);
        log.error("Error message: {} ", message);

    }
}
