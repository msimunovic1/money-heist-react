package hr.msimunovic.moneyheist.api.exception.handler;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GlobalExceptionResponse {

    private LocalDateTime timestamp;
    private Integer status;
    private List<String> message;

}