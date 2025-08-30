package kr.hs.sdh.digitechnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 기자재 통계 정보 DTO
 * 기자재의 전체적인 현황을 나타내는 데이터 전송 객체
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentStatisticsDto {
    
    /**
     * 전체 기자재 수
     */
    private long totalCount;
    
    /**
     * 사용 가능한 기자재 수
     */
    private long availableCount;
    
    /**
     * 대여 중인 기자재 수
     */
    private long rentedCount;
    
    /**
     * 고장난 기자재 수
     */
    private long brokenCount;
    
    /**
     * 공개된 기자재 수
     */
    private long publicCount;
}
