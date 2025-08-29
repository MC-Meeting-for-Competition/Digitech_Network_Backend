package kr.hs.sdh.digitechnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
enum EquipmentStatus {
    AVAILABLE("대여 가능한 기자재"),
    RENT("대여 중인 기자재"),
    BROKEN("고장난 기자재"),
    CHECK("반납 처리 중인 기자재"),
    UNAVAILABLE("사용이 불가능한 기자재"),
    FIX("수리 중인 기자재");

    private final String message;

    EquipmentStatus(String message) {
        this.message = message;
    }
}

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
    private String identifier;

    @Column(nullable = false)
    private Boolean isPublic;

    @Enumerated(EnumType.STRING)
    private EquipmentStatus status;

    @Column(nullable = true)
    private String cautionMessage;

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
}
