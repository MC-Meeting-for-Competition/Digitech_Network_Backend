package kr.hs.sdh.digitechnetwork.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 에러 응답을 위한 DTO
 * 클라이언트에게 일관된 형태의 에러 정보를 제공
 * 
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Getter
@Builder
public class ErrorResponse {
    
    /**
     * 에러 발생 시간
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;
    
    /**
     * HTTP 상태 코드
     */
    private final int status;
    
    /**
     * 비즈니스 에러 코드
     */
    private final String code;
    
    /**
     * 에러 메시지
     */
    private final String message;
    
    /**
     * 상세 에러 메시지
     */
    private final String detailMessage;
    
    /**
     * 요청 경로
     */
    private final String path;
    
    /**
     * 필드별 에러 정보 (유효성 검증 실패 시)
     */
    private final List<FieldError> fieldErrors;
    
    /**
     * 필드별 에러 정보를 담는 내부 클래스
     */
    @Getter
    @Builder
    public static class FieldError {
        private final String field;
        private final String value;
        private final String reason;
    }
    
    /**
     * BusinessException으로부터 ErrorResponse 생성
     */
    public static ErrorResponse of(BusinessException e, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(e.getErrorCode().getStatus())
                .code(e.getErrorCode().getCode())
                .message(e.getErrorCode().getMessage())
                .detailMessage(e.getDetailMessage())
                .path(path)
                .build();
    }
    
    /**
     * ErrorCode와 상세 메시지로부터 ErrorResponse 생성
     */
    public static ErrorResponse of(ErrorCode errorCode, String detailMessage, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .detailMessage(detailMessage)
                .path(path)
                .build();
    }
    
    /**
     * ErrorCode로부터 ErrorResponse 생성
     */
    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .detailMessage(errorCode.getMessage())
                .path(path)
                .build();
    }
}
