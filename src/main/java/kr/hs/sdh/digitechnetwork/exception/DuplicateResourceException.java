package kr.hs.sdh.digitechnetwork.exception;

/**
 * 중복된 리소스가 있을 때 발생하는 예외
 * 고유해야 하는 값이 이미 존재할 때 사용
 * 
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
public class DuplicateResourceException extends BusinessException {
    
    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(ErrorCode.DUPLICATE_RESOURCE, 
              String.format("%s already exists with %s: %s", resourceName, fieldName, fieldValue));
    }
    
    public DuplicateResourceException(String resourceName, String identifier) {
        this(resourceName, "identifier", identifier);
    }
    
    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue, String detailMessage) {
        super(ErrorCode.DUPLICATE_RESOURCE, detailMessage);
    }
}
