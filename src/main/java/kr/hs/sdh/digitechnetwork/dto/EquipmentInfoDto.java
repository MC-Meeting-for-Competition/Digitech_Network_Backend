package kr.hs.sdh.digitechnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentInfoDto {
    private Long equipmentId;
    private String name;
    private String status;
    private String identifier;
    private String description;
    private String cautionMessage;
    private String type;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdateTime;
}
