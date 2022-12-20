package org.example;

import lombok.Data;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;

@Data
public class ErrorResponseBody {
    private ZonedDateTime timestamp;
    private HttpStatusCode status;
    private String errorMessage;

    private ErrorResponseBody(ZonedDateTime timestamp, HttpStatusCode status, String errorMessage) {
        this.timestamp = timestamp;
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public static ResponseEntity errorResponse(HttpStatusCode status, String errorMessage) {
        ErrorResponseBody errorResponseBody = new ErrorResponseBody(ZonedDateTime.now(),
                status,
                errorMessage);

        return ResponseEntity.status(status).body(errorResponseBody);
    }
}
