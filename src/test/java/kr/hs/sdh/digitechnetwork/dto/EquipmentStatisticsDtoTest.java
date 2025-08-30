package kr.hs.sdh.digitechnetwork.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EquipmentStatisticsDto 테스트 클래스
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 */
@DisplayName("EquipmentStatisticsDto 테스트")
class EquipmentStatisticsDtoTest {

    private EquipmentStatisticsDto statisticsDto;

    @BeforeEach
    void setUp() {
        statisticsDto = EquipmentStatisticsDto.builder()
                .totalCount(100)
                .availableCount(80)
                .rentedCount(15)
                .brokenCount(5)
                .publicCount(90)
                .build();
    }

    @Test
    @DisplayName("Builder 패턴으로 EquipmentStatisticsDto 생성 테스트")
    void testBuilderPattern() {
        // Given & When
        EquipmentStatisticsDto dto = EquipmentStatisticsDto.builder()
                .totalCount(50)
                .availableCount(30)
                .rentedCount(10)
                .brokenCount(5)
                .publicCount(45)
                .build();

        // Then
        assertNotNull(dto);
        assertEquals(50, dto.getTotalCount());
        assertEquals(30, dto.getAvailableCount());
        assertEquals(10, dto.getRentedCount());
        assertEquals(5, dto.getBrokenCount());
        assertEquals(45, dto.getPublicCount());
    }

    @Test
    @DisplayName("Getter 메서드 테스트")
    void testGetterMethods() {
        // Then
        assertEquals(100, statisticsDto.getTotalCount());
        assertEquals(80, statisticsDto.getAvailableCount());
        assertEquals(15, statisticsDto.getRentedCount());
        assertEquals(5, statisticsDto.getBrokenCount());
        assertEquals(90, statisticsDto.getPublicCount());
    }

    @Test
    @DisplayName("NoArgsConstructor 테스트")
    void testNoArgsConstructor() {
        // Given & When
        EquipmentStatisticsDto dto = new EquipmentStatisticsDto();

        // Then
        assertNotNull(dto);
        assertEquals(0, dto.getTotalCount());
        assertEquals(0, dto.getAvailableCount());
        assertEquals(0, dto.getRentedCount());
        assertEquals(0, dto.getBrokenCount());
        assertEquals(0, dto.getPublicCount());
    }

    @Test
    @DisplayName("AllArgsConstructor 테스트")
    void testAllArgsConstructor() {
        // Given & When
        EquipmentStatisticsDto dto = new EquipmentStatisticsDto(200, 150, 30, 20, 180);

        // Then
        assertNotNull(dto);
        assertEquals(200, dto.getTotalCount());
        assertEquals(150, dto.getAvailableCount());
        assertEquals(30, dto.getRentedCount());
        assertEquals(20, dto.getBrokenCount());
        assertEquals(180, dto.getPublicCount());
    }

    @Test
    @DisplayName("통계 데이터 유효성 검증 테스트")
    void testStatisticsValidation() {
        // Given
        long total = statisticsDto.getTotalCount();
        long available = statisticsDto.getAvailableCount();
        long rented = statisticsDto.getRentedCount();
        long broken = statisticsDto.getBrokenCount();

        // Then
        assertTrue(total >= 0, "전체 수는 0 이상이어야 함");
        assertTrue(available >= 0, "사용 가능한 수는 0 이상이어야 함");
        assertTrue(rented >= 0, "대여 중인 수는 0 이상이어야 함");
        assertTrue(broken >= 0, "고장난 수는 0 이상이어야 함");
        assertTrue(total >= (available + rented + broken), "전체 수는 세부 항목의 합보다 크거나 같아야 함");
    }
}
