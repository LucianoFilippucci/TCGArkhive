package it.lucianofilippucci.tcgarkhive.API.V1.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.lucianofilippucci.tcgarkhive.helpers.enums.ErrorCodes;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResponse<T> {
    protected LocalDateTime timestamp;
    protected Integer statusCode;
    protected String message;
    protected T data;
    protected ErrorCodes errorCode;
}
