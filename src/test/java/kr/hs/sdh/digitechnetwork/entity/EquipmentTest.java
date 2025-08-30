package kr.hs.sdh.digitechnetwork.entity;

import kr.hs.sdh.digitechnetwork.enums.EquipmentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Equipment 엔티티 테스트 클래스
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 */
@DisplayName("Equipment 엔티티 테스트")
class EquipmentTest {

    private Equipment equipment;
    private EquipmentType equipmentType;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        
        equipmentType = EquipmentType.builder()
                .id(1L)
                .type("노트북")
                .description("학생용 노트북")
                .isPublic(true)
                .build();

        equipment = Equipment.builder()
                .id(1L)
                .identifier("NB-001")
                .status(EquipmentStatus.AVAILABLE)
                .isPublic(true)
                .cautionMessage("주의: 물에 닿지 않도록 주의하세요")
                .equipmentType(equipmentType)
                .build();
    }

    @Test
    @DisplayName("Equipment 빌더 패턴 테스트")
    void testBuilderPattern() {
        // Given & When
        Equipment newEquipment = Equipment.builder()
                .id(2L)
                .identifier("NB-002")
                .status(EquipmentStatus.RENT)
                .isPublic(false)
                .cautionMessage("대여 중")
                .equipmentType(equipmentType)
                .build();

        // Then
        assertNotNull(newEquipment);
        assertEquals(2L, newEquipment.getId());
        assertEquals("NB-002", newEquipment.getIdentifier());
        assertEquals(EquipmentStatus.RENT, newEquipment.getStatus());
        assertFalse(newEquipment.getIsPublic());
        assertEquals("대여 중", newEquipment.getCautionMessage());
        assertEquals(equipmentType, newEquipment.getEquipmentType());
    }

    @Test
    @DisplayName("Equipment 기본 생성자 테스트")
    void testDefaultConstructor() {
        // Given & When
        Equipment newEquipment = new Equipment();

        // Then
        assertNotNull(newEquipment);
        assertNull(newEquipment.getId());
        assertNull(newEquipment.getIdentifier());
        assertSame(EquipmentStatus.AVAILABLE, newEquipment.getStatus());
        assertTrue(newEquipment.getIsPublic());
        assertNull(newEquipment.getCautionMessage());
        assertNull(newEquipment.getEquipmentType());
    }

    @Test
    @DisplayName("Equipment Setter 메서드 테스트")
    void testSetterMethods() {
        // Given
        Equipment newEquipment = new Equipment();
        EquipmentType newType = EquipmentType.builder()
                .id(2L)
                .type("프로젝터")
                .description("강의실용 프로젝터")
                .isPublic(true)
                .build();

        // When
        newEquipment.setId(3L);
        newEquipment.setIdentifier("PRJ-001");
        newEquipment.setStatus(EquipmentStatus.BROKEN);
        newEquipment.setIsPublic(false);
        newEquipment.setCautionMessage("수리 필요");
        newEquipment.setEquipmentType(newType);

        // Then
        assertEquals(3L, newEquipment.getId());
        assertEquals("PRJ-001", newEquipment.getIdentifier());
        assertEquals(EquipmentStatus.BROKEN, newEquipment.getStatus());
        assertFalse(newEquipment.getIsPublic());
        assertEquals("수리 필요", newEquipment.getCautionMessage());
        assertEquals(newType, newEquipment.getEquipmentType());
    }

    @Test
    @DisplayName("Equipment 상태 변경 테스트")
    void testStatusChange() {
        // Given
        EquipmentStatus originalStatus = equipment.getStatus();

        // When
        equipment.setStatus(EquipmentStatus.RENT);

        // Then
        assertNotEquals(originalStatus, equipment.getStatus());
        assertEquals(EquipmentStatus.RENT, equipment.getStatus());
    }

    @Test
    @DisplayName("Equipment 공개 여부 변경 테스트")
    void testPublicityChange() {
        // Given
        Boolean originalPublicity = equipment.getIsPublic();

        // When
        equipment.setIsPublic(false);

        // Then
        assertNotEquals(originalPublicity, equipment.getIsPublic());
        assertFalse(equipment.getIsPublic());
    }

    @Test
    @DisplayName("Equipment 식별자 변경 테스트")
    void testIdentifierChange() {
        // Given
        String originalIdentifier = equipment.getIdentifier();

        // When
        equipment.setIdentifier("NB-001-UPDATED");

        // Then
        assertNotEquals(originalIdentifier, equipment.getIdentifier());
        assertEquals("NB-001-UPDATED", equipment.getIdentifier());
    }

    @Test
    @DisplayName("Equipment 주의사항 변경 테스트")
    void testCautionMessageChange() {
        // Given
        String originalMessage = equipment.getCautionMessage();

        // When
        equipment.setCautionMessage("새로운 주의사항: 배터리 수명에 주의하세요");

        // Then
        assertNotEquals(originalMessage, equipment.getCautionMessage());
        assertEquals("새로운 주의사항: 배터리 수명에 주의하세요", equipment.getCautionMessage());
    }

    @Test
    @DisplayName("Equipment 타입 변경 테스트")
    void testEquipmentTypeChange() {
        // Given
        EquipmentType originalType = equipment.getEquipmentType();
        EquipmentType newType = EquipmentType.builder()
                .id(3L)
                .type("태블릿")
                .description("학생용 태블릿")
                .isPublic(true)
                .build();

        // When
        equipment.setEquipmentType(newType);

        // Then
        assertNotEquals(originalType, equipment.getEquipmentType());
        assertEquals(newType, equipment.getEquipmentType());
        assertEquals("태블릿", equipment.getEquipmentType().getType());
    }

    @Test
    @DisplayName("Equipment 유효성 검증 테스트")
    void testEquipmentValidation() {
        // Then
        assertNotNull(equipment.getId(), "ID는 null이 아니어야 함");
        assertNotNull(equipment.getIdentifier(), "식별자는 null이 아니어야 함");
        assertNotNull(equipment.getStatus(), "상태는 null이 아니어야 함");
        assertNotNull(equipment.getIsPublic(), "공개 여부는 null이 아니어야 함");
        assertNotNull(equipment.getEquipmentType(), "장비 타입은 null이 아니어야 함");
    }
}
