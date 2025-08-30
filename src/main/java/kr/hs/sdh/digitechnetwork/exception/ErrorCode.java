package kr.hs.sdh.digitechnetwork.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
    
    // 공통 에러 (1000번대)
    INVALID_INPUT_VALUE(1000, "C001", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(1001, "C002", "허용되지 않은 HTTP 메서드입니다."),
    ENTITY_NOT_FOUND(1002, "C003", "엔티티를 찾을 수 없습니다."),
    INVALID_TYPE_VALUE(1003, "C004", "잘못된 타입의 값입니다."),
    HANDLE_ACCESS_DENIED(1004, "C005", "접근이 거부되었습니다."),
    
    // 비즈니스 에러 (2000번대)
    RESOURCE_NOT_FOUND(2000, "B001", "요청한 리소스를 찾을 수 없습니다."),
    DUPLICATE_RESOURCE(2001, "B002", "이미 존재하는 리소스입니다."),
    INVALID_STATUS_CHANGE(2002, "B003", "잘못된 상태 변경입니다."),
    INSUFFICIENT_PERMISSION(2003, "B004", "권한이 부족합니다."),
    BUSINESS_RULE_VIOLATION(2004, "B005", "비즈니스 규칙을 위반했습니다."),
    
    // 사용자 관련 에러 (3000번대)
    USER_NOT_FOUND(3000, "U001", "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(3001, "U002", "이미 사용 중인 이메일입니다."),
    DUPLICATE_PHONE_NUMBER(3002, "U003", "이미 사용 중인 전화번호입니다."),
    INVALID_CREDENTIALS(3003, "U004", "잘못된 인증 정보입니다."),
    ACCOUNT_DISABLED(3004, "U005", "비활성화된 계정입니다."),
    
    // 기자재 관련 에러 (4000번대)
    EQUIPMENT_NOT_FOUND(4000, "E001", "기자재를 찾을 수 없습니다."),
    DUPLICATE_EQUIPMENT_IDENTIFIER(4001, "E002", "이미 존재하는 기자재 식별자입니다."),
    EQUIPMENT_NOT_AVAILABLE(4002, "E003", "사용할 수 없는 기자재입니다."),
    EQUIPMENT_ALREADY_RENTED(4003, "E004", "이미 대여 중인 기자재입니다."),
    EQUIPMENT_TYPE_NOT_FOUND(4004, "E005", "기자재 타입을 찾을 수 없습니다."),
    
    // 대여 관련 에러 (5000번대)
    RENT_HISTORY_NOT_FOUND(5000, "R001", "대여 이력을 찾을 수 없습니다."),
    RENT_ALREADY_EXISTS(5001, "R002", "이미 존재하는 대여 기록입니다."),
    RENT_NOT_ALLOWED(5002, "R003", "대여가 허용되지 않습니다."),
    RENT_PERIOD_EXPIRED(5003, "R004", "대여 기간이 만료되었습니다."),
    
    // 시스템 에러 (9000번대)
    INTERNAL_SERVER_ERROR(9000, "S001", "내부 서버 오류가 발생했습니다."),
    EXTERNAL_API_ERROR(9001, "S002", "외부 API 호출 중 오류가 발생했습니다."),
    DATABASE_ERROR(9002, "S003", "데이터베이스 오류가 발생했습니다."),
    FILE_PROCESSING_ERROR(9003, "S004", "파일 처리 중 오류가 발생했습니다.");
    
    private final int status;
    private final String code;
    private final String message;
}
