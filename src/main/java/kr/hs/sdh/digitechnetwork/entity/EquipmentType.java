package kr.hs.sdh.digitechnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Table(name = "equipment_types")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EquipmentType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String type;

    @Column(nullable = true)
    private String imageUrl;

    @Column(nullable = false)
    private Boolean isPublic;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "equipments_id")
    Collection<Equipment> equipmentList;

    public EquipmentType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[" + id + "]" + type);
        for (Equipment equipment : equipmentList) {
            result.append("\n").append(equipment.toString());
        }

        return result.toString();
    }
}
