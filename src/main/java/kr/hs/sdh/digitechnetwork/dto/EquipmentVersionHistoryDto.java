package kr.hs.sdh.digitechnetwork.dto;

import kr.hs.sdh.digitechnetwork.enums.EquipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 기자재 버전 히스토리 DTO
 * 기자재의 생성/수정 이력을 나타내는 데이터 전송 객체
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentVersionHistoryDto {
    
    /**
     * 기자재 ID
     */
    private Long equipmentId;
    
    /**
     * 기자재 식별자
     */
    private String identifier;
    
    /**
     * 생성 시간
     */
    private LocalDateTime createdAt;
    
    /**
     * 마지막 수정 시간
     */
    private LocalDateTime lastUpdateTime;
    
    /**
     * 현재 상태
     */
    private EquipmentStatus currentStatus;
    
    /**
     * 현재 공개 여부
     */
    private Boolean currentPublicity;
}
