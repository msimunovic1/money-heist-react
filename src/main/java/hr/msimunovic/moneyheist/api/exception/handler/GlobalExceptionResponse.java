package hr.msimunovic.moneyheist.api.exception.handler;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GlobalExceptionResponse {

    private LocalDateTime timestamp;
    private Integer status;
    private String message;

}