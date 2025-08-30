package kr.hs.sdh.digitechnetwork.dto;

import kr.hs.sdh.digitechnetwork.enums.EquipmentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EquipmentVersionHistoryDto 테스트 클래스
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 */
@DisplayName("EquipmentVersionHistoryDto 테스트")
class EquipmentVersionHistoryDtoTest {

    private EquipmentVersionHistoryDto versionHistoryDto;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdateTime;

    @BeforeEach
    void setUp() {
        createdAt = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        lastUpdateTime = LocalDateTime.of(2025, 8, 30, 15, 30, 0);
        
        versionHistoryDto = EquipmentVersionHistoryDto.builder()
                .equipmentId(1L)
                .identifier("EQ-001")
                .createdAt(createdAt)
                .lastUpdateTime(lastUpdateTime)
                .currentStatus(EquipmentStatus.AVAILABLE)
                .currentPublicity(true)
                .build();
    }

    @Test
    @DisplayName("Builder 패턴으로 EquipmentVersionHistoryDto 생성 테스트")
    void testBuilderPattern() {
        // Given & When
        EquipmentVersionHistoryDto dto = EquipmentVersionHistoryDto.builder()
                .equipmentId(2L)
                .identifier("EQ-002")
                .createdAt(createdAt)
                .lastUpdateTime(lastUpdateTime)
                .currentStatus(EquipmentStatus.RENT)
                .currentPublicity(false)
                .build();

        // Then
        assertNotNull(dto);
        assertEquals(2L, dto.getEquipmentId());
        assertEquals("EQ-002", dto.getIdentifier());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(lastUpdateTime, dto.getLastUpdateTime());
        assertEquals(EquipmentStatus.RENT, dto.getCurrentStatus());
        assertFalse(dto.getCurrentPublicity());
    }

    @Test
    @DisplayName("Getter 메서드 테스트")
    void testGetterMethods() {
        // Then
        assertEquals(1L, versionHistoryDto.getEquipmentId());
        assertEquals("EQ-001", versionHistoryDto.getIdentifier());
        assertEquals(createdAt, versionHistoryDto.getCreatedAt());
        assertEquals(lastUpdateTime, versionHistoryDto.getLastUpdateTime());
        assertEquals(EquipmentStatus.AVAILABLE, versionHistoryDto.getCurrentStatus());
        assertTrue(versionHistoryDto.getCurrentPublicity());
    }

    @Test
    @DisplayName("NoArgsConstructor 테스트")
    void testNoArgsConstructor() {
        // Given & When
        EquipmentVersionHistoryDto dto = new EquipmentVersionHistoryDto();

        // Then
        assertNotNull(dto);
        assertNull(dto.getEquipmentId());
        assertNull(dto.getIdentifier());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getLastUpdateTime());
        assertNull(dto.getCurrentStatus());
        assertNull(dto.getCurrentPublicity());
    }

    @Test
    @DisplayName("AllArgsConstructor 테스트")
    void testAllArgsConstructor() {
        // Given & When
        EquipmentVersionHistoryDto dto = new EquipmentVersionHistoryDto(
                3L, "EQ-003", createdAt, lastUpdateTime, 
                EquipmentStatus.BROKEN, false
        );

        // Then
        assertNotNull(dto);
        assertEquals(3L, dto.getEquipmentId());
        assertEquals("EQ-003", dto.getIdentifier());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(lastUpdateTime, dto.getLastUpdateTime());
        assertEquals(EquipmentStatus.BROKEN, dto.getCurrentStatus());
        assertFalse(dto.getCurrentPublicity());
    }

    @Test
    @DisplayName("시간 순서 검증 테스트")
    void testTimeOrderValidation() {
        // Given
        LocalDateTime futureTime = LocalDateTime.of(2025, 12, 31, 23, 59, 59);
        
        // When
        EquipmentVersionHistoryDto dto = EquipmentVersionHistoryDto.builder()
                .equipmentId(4L)
                .identifier("EQ-004")
                .createdAt(createdAt)
                .lastUpdateTime(futureTime)
                .currentStatus(EquipmentStatus.AVAILABLE)
                .currentPublicity(true)
                .build();

        // Then
        assertTrue(dto.getCreatedAt().isBefore(dto.getLastUpdateTime()), 
                "생성 시간은 마지막 수정 시간보다 이전이어야 함");
    }

    @Test
    @DisplayName("EquipmentStatus enum 값 테스트")
    void testEquipmentStatusValues() {
        // Given & When
        EquipmentVersionHistoryDto availableDto = EquipmentVersionHistoryDto.builder()
                .equipmentId(5L)
                .identifier("EQ-005")
                .createdAt(createdAt)
                .lastUpdateTime(lastUpdateTime)
                .currentStatus(EquipmentStatus.AVAILABLE)
                .currentPublicity(true)
                .build();

        EquipmentVersionHistoryDto rentedDto = EquipmentVersionHistoryDto.builder()
                .equipmentId(6L)
                .identifier("EQ-006")
                .createdAt(createdAt)
                .lastUpdateTime(lastUpdateTime)
                .currentStatus(EquipmentStatus.RENT)
                .currentPublicity(true)
                .build();

        // Then
        assertEquals(EquipmentStatus.AVAILABLE, availableDto.getCurrentStatus());
        assertEquals(EquipmentStatus.RENT, rentedDto.getCurrentStatus());
        assertNotEquals(availableDto.getCurrentStatus(), rentedDto.getCurrentStatus());
    }
}
