package kr.hs.sdh.digitechnetwork.entity;

import jakarta.persistence.*;
import kr.hs.sdh.digitechnetwork.enums.EquipmentStatus;
import lombok.*;

/**
 * Equipment 엔티티
 *
 * @since 2025.08.29
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 0.0.1
 */
@Entity
@Table(name = "equipments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Equipment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipments_id")
    private Long id;

    @Column(unique = true)
    private String identifier; // 관리자가 입력한 식별자

    @Column(nullable = false)
    private Boolean isPublic; // 사용자에게 공개 여부

    @Enumerated(EnumType.STRING)
    private EquipmentStatus status; // 기자재 상태

    @Column(nullable = true)
    private String cautionMessage; // 주의사항 메시지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_type_id")
    private EquipmentType equipmentType;

    Equipment setStatus(EquipmentStatus status) {
        this.status = status;
        return this;
    }

    Equipment setPublic(Boolean isPublic) {
        this.isPublic = isPublic;
        return this;
    }

    Equipment setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    Equipment setCautionMessage(String cautionMessage) {
        this.cautionMessage = cautionMessage;
        return this;
    }

    @Override
    public String toString() {
        return "Equipment [" +
                "id=" + id + ", " +
                "identifier=" + identifier + ", " +
                "isPublic=" + isPublic + ", " +
                "status=" + status + ", " +
                "cautionMessage=" + cautionMessage + "]";
    }
}
