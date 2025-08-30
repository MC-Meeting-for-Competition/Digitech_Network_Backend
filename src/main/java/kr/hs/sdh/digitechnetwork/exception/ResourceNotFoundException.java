package kr.hs.sdh.digitechnetwork.exception;

/**
 * 리소스를 찾을 수 없을 때 발생하는 예외
 * 데이터베이스에서 요청된 엔티티를 찾을 수 없을 때 사용
 * 
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
public class ResourceNotFoundException extends BusinessException {
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(ErrorCode.RESOURCE_NOT_FOUND, 
              String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue));
    }
    
    public ResourceNotFoundException(String resourceName, Long id) {
        this(resourceName, "id", id);
    }
    
    public ResourceNotFoundException(String resourceName, String identifier) {
        this(resourceName, "identifier", identifier);
    }
}
