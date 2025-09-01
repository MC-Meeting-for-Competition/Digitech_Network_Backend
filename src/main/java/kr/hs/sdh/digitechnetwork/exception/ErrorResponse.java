package kr.hs.sdh.digitechnetwork.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String code;
    private String message;
    private String detailMessage;
    private String path;
    private List<FieldError> fieldErrors;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String value;
        private String reason;
    }

    public static ErrorResponse of(HttpStatus status, String code, String message, String detailMessage, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .code(code)
                .message(message)
                .detailMessage(detailMessage)
                .path(path)
                .build();
    }

    public static ErrorResponse of(HttpStatus status, String code, String message, String path) {
        return of(status, code, message, message, path);
    }
}
