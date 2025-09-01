package kr.hs.sdh.digitechnetwork.dto;

import kr.hs.sdh.digitechnetwork.enums.EquipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 기자재 등록 요청 DTO
 * 
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentCreateRequestDto {
    
    @NotBlank(message = "기자재 식별자는 필수입니다.")
    private String identifier;
    
    @NotBlank(message = "기자재 이름은 필수입니다.")
    private String name;
    
    private String description;
    
    private String cautionMessage;
    
    @NotNull(message = "기자재 타입은 필수입니다.")
    private Long equipmentTypeId;
    
    private EquipmentStatus status;
    
    private Boolean isPublic;
}
