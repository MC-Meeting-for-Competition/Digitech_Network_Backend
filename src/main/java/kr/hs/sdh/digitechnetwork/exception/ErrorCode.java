package kr.hs.sdh.digitechnetwork.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 애플리케이션 오류 코드 정의
 * 모든 비즈니스 오류에 대한 코드와 메시지를 중앙 관리
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    
    // 공통 오류
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "C002", "접근 권한이 없습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "요청한 리소스를 찾을 수 없습니다."),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "C004", "이미 존재하는 리소스입니다."),
    
    // OAuth 관련 오류
    OAUTH_AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "O001", "OAuth 인증에 실패했습니다."),
    OAUTH_TOKEN_REQUEST_FAILED(HttpStatus.BAD_REQUEST, "O002", "OAuth 토큰 요청에 실패했습니다."),
    OAUTH_USER_INFO_REQUEST_FAILED(HttpStatus.BAD_REQUEST, "O003", "OAuth 사용자 정보 요청에 실패했습니다."),
    
    // 사용자 관련 오류
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "U002", "이미 존재하는 사용자입니다."),
    USER_DISABLED(HttpStatus.FORBIDDEN, "U003", "비활성화된 사용자입니다."),
    
    // 기자재 관련 오류
    EQUIPMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "E001", "기자재를 찾을 수 없습니다."),
    EQUIPMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "E002", "이미 존재하는 기자재입니다."),
    EQUIPMENT_UNAVAILABLE(HttpStatus.BAD_REQUEST, "E003", "사용할 수 없는 기자재입니다."),
    
    // 대여 관련 오류
    RENT_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "대여 기록을 찾을 수 없습니다."),
    RENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "R002", "이미 대여 중인 기자재입니다."),
    RENT_PERIOD_INVALID(HttpStatus.BAD_REQUEST, "R003", "잘못된 대여 기간입니다."),
    
    // 시스템 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "내부 서버 오류가 발생했습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S002", "데이터베이스 오류가 발생했습니다."),
    EXTERNAL_SERVICE_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "S003", "외부 서비스 오류가 발생했습니다.");
    
    private final HttpStatus status;
    private final String code;
    private final String message;
}
