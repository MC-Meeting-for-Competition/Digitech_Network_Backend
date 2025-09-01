package kr.hs.sdh.digitechnetwork.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 애플리케이션에서 사용하는 에러 코드 정의
 * HTTP 상태 코드와 비즈니스 에러 코드를 매핑
 * 
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common Errors
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "지원하지 않는 HTTP 메서드입니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "요청한 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C004", "서버 내부 오류가 발생했습니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C005", "잘못된 타입의 값입니다."),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "C006", "접근이 거부되었습니다."),
    
    // Business Errors
    BUSINESS_EXCEPTION(HttpStatus.BAD_REQUEST, "B001", "비즈니스 로직 오류가 발생했습니다."),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "B002", "이미 존재하는 리소스입니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "B003", "요청한 리소스를 찾을 수 없습니다."),
    
    // User Errors
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),
    DUPLICATE_USER(HttpStatus.CONFLICT, "U002", "이미 존재하는 사용자입니다."),
    INVALID_USER_CREDENTIALS(HttpStatus.UNAUTHORIZED, "U003", "잘못된 사용자 인증 정보입니다."),
    USER_DISABLED(HttpStatus.FORBIDDEN, "U004", "비활성화된 사용자입니다."),
    
    // Equipment Errors
    EQUIPMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "E001", "장비를 찾을 수 없습니다."),
    EQUIPMENT_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "E002", "사용할 수 없는 장비입니다."),
    EQUIPMENT_ALREADY_RENTED(HttpStatus.CONFLICT, "E003", "이미 대여 중인 장비입니다."),
    EQUIPMENT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "E004", "장비 타입을 찾을 수 없습니다."),
    DUPLICATE_EQUIPMENT(HttpStatus.CONFLICT, "E005", "이미 존재하는 장비입니다."),
    
    // Rent Errors
    RENT_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "대여 기록을 찾을 수 없습니다."),
    RENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "R002", "이미 대여 중인 기록이 있습니다."),
    RENT_NOT_ACTIVE(HttpStatus.BAD_REQUEST, "R003", "활성화되지 않은 대여 기록입니다."),
    
    // OAuth Errors
    OAUTH_AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "O001", "OAuth 인증에 실패했습니다."),
    OAUTH_TOKEN_REQUEST_FAILED(HttpStatus.BAD_REQUEST, "O002", "OAuth 토큰 요청에 실패했습니다."),
    OAUTH_USER_INFO_REQUEST_FAILED(HttpStatus.BAD_REQUEST, "O003", "OAuth 사용자 정보 요청에 실패했습니다."),
    OAUTH_INVALID_CODE(HttpStatus.BAD_REQUEST, "O004", "잘못된 OAuth 인증 코드입니다."),
    OAUTH_INVALID_EMAIL(HttpStatus.BAD_REQUEST, "O005", "올바르지 않은 이메일 주소입니다."),

    // System Errors
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "데이터베이스 오류가 발생했습니다."),
    EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S002", "외부 API 호출 중 오류가 발생했습니다."),
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S003", "파일 업로드 중 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
