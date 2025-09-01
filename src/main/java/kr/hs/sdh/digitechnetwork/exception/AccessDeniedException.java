package kr.hs.sdh.digitechnetwork.exception;

/**
 * 접근 권한 부족 예외
 * 요청한 리소스에 대한 접근 권한이 없을 때 발생
 * 
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
public class AccessDeniedException extends BusinessException {
    
    public AccessDeniedException(String resource, String operation) {
        super(ErrorCode.ACCESS_DENIED, 
              String.format("리소스 '%s'에 대한 '%s' 권한이 없습니다.", resource, operation));
    }
    
    public AccessDeniedException(String message) {
        super(ErrorCode.ACCESS_DENIED, message);
    }
    
    public AccessDeniedException(String message, Throwable cause) {
        super(ErrorCode.ACCESS_DENIED, message, cause);
    }
}
