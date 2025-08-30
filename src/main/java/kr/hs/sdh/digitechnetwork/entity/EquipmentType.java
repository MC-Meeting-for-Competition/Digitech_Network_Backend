package kr.hs.sdh.digitechnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipment_types")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EquipmentType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_type_id")
    @Setter
    private Long id;

    @Column(unique = true, nullable = false)
    private String type;

    @Column
    private String description;

    @Column
    private Boolean isPublic = true;

    @OneToMany(mappedBy = "equipmentType", fetch = FetchType.LAZY)
    private List<Equipment> equipmentList = new ArrayList<>();

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public void setEquipmentList(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }

    // 편의 메서드
    public int getEquipmentCount() {
        return equipmentList != null ? equipmentList.size() : 0;
    }

    public boolean hasEquipment() {
        return equipmentList != null && !equipmentList.isEmpty();
    }
}