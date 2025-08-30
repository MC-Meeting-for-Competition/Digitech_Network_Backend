package kr.hs.sdh.digitechnetwork.entity;

import jakarta.persistence.*;
import kr.hs.sdh.digitechnetwork.enums.EquipmentStatus;
import lombok.*;

@Entity
@Table(name = "equipments")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Equipment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    @Setter
    private Long id;

    @Column(unique = true, nullable = false)
    private String identifier;

    @Column
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private EquipmentStatus status = EquipmentStatus.AVAILABLE;

    @Column
    private Boolean isPublic = true;

    @Column
    private String cautionMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_type_id")
    private EquipmentType equipmentType;

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public void setCautionMessage(String cautionMessage) {
        this.cautionMessage = cautionMessage;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    // 편의 메서드
    public String getTypeName() {
        return equipmentType != null ? equipmentType.getType() : "미분류";
    }

    public boolean isAvailable() {
        return EquipmentStatus.AVAILABLE.equals(this.status);
    }

    public boolean isRented() {
        return EquipmentStatus.RENT.equals(this.status);
    }

    public boolean isBroken() {
        return EquipmentStatus.BROKEN.equals(this.status);
    }
}