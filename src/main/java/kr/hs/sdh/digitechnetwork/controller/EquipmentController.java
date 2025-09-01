package kr.hs.sdh.digitechnetwork.controller;

import kr.hs.sdh.digitechnetwork.dto.EquipmentCreateRequestDto;
import kr.hs.sdh.digitechnetwork.dto.EquipmentInfoDto;
import kr.hs.sdh.digitechnetwork.dto.EquipmentStatisticsDto;
import kr.hs.sdh.digitechnetwork.dto.EquipmentUpdateRequestDto;
import kr.hs.sdh.digitechnetwork.dto.EquipmentVersionHistoryDto;
import kr.hs.sdh.digitechnetwork.entity.Equipment;
import kr.hs.sdh.digitechnetwork.entity.EquipmentType;
import kr.hs.sdh.digitechnetwork.enums.EquipmentStatus;
import kr.hs.sdh.digitechnetwork.exception.ResourceNotFoundException;
import kr.hs.sdh.digitechnetwork.repository.EquipmentTypeRepository;
import kr.hs.sdh.digitechnetwork.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Equipment REST API 컨트롤러
 * 기자재 관련 HTTP 요청을 처리
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/equipment")
@RequiredArgsConstructor
@Validated
public class EquipmentController {
    
    private final EquipmentService equipmentService;
    private final EquipmentTypeRepository equipmentTypeRepository;

    /**
     * 모든 기자재 목록 조회
     * @return 기자재 정보 목록
     */
    @GetMapping
    public ResponseEntity<List<EquipmentInfoDto>> getEquipmentList() {
        log.info("기자재 목록 조회 요청");
        List<EquipmentInfoDto> equipmentList = equipmentService.getAllEquipments();
        return ResponseEntity.ok(equipmentList);
    }

    /**
     * 특정 기자재 조회
     * @param id 기자재 ID
     * @return 기자재 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<EquipmentInfoDto> getEquipmentById(@PathVariable Long id) {
        log.info("기자재 조회 요청: ID={}", id);
        
        Optional<Equipment> equipmentOpt = equipmentService.getEquipmentById(id);
        
        if (equipmentOpt.isEmpty()) {
            throw new ResourceNotFoundException("Equipment", id);
        }
        
        Equipment equipment = equipmentOpt.get();
        EquipmentInfoDto equipmentInfoDto = convertToEquipmentInfoDto(equipment);
        
        return ResponseEntity.ok(equipmentInfoDto);
    }

    /**
     * 기자재 등록 (관리자만)
     * @param requestDto 등록할 기자재 정보
     * @return 등록된 기자재 정보
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EquipmentInfoDto> registerEquipment(@Valid @RequestBody EquipmentCreateRequestDto requestDto) {
        log.info("기자재 등록 요청 (관리자): {}", requestDto.getIdentifier());
        
        // EquipmentType 조회
        EquipmentType equipmentType = equipmentTypeRepository.findById(requestDto.getEquipmentTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("EquipmentType", requestDto.getEquipmentTypeId()));
        
        // Equipment 엔티티 생성
        Equipment equipment = Equipment.builder()
                .identifier(requestDto.getIdentifier())
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .cautionMessage(requestDto.getCautionMessage())
                .equipmentType(equipmentType)
                .status(requestDto.getStatus() != null ? requestDto.getStatus() : EquipmentStatus.AVAILABLE)
                .isPublic(requestDto.getIsPublic() != null ? requestDto.getIsPublic() : true)
                .build();
        
        Equipment savedEquipment = equipmentService.registerEquipment(equipment);
        EquipmentInfoDto equipmentInfoDto = convertToEquipmentInfoDto(savedEquipment);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(equipmentInfoDto);
    }

    /**
     * 기자재 정보 수정 (관리자만)
     * @param id 기자재 ID
     * @param requestDto 수정할 기자재 정보
     * @return 수정된 기자재 정보
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EquipmentInfoDto> updateEquipment(@PathVariable Long id, @Valid @RequestBody EquipmentUpdateRequestDto requestDto) {
        log.info("기자재 수정 요청 (관리자): ID={}", id);
        
        // EquipmentType 조회
        EquipmentType equipmentType = equipmentTypeRepository.findById(requestDto.getEquipmentTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("EquipmentType", requestDto.getEquipmentTypeId()));
        
        // Equipment 엔티티 생성
        Equipment equipment = Equipment.builder()
                .identifier(requestDto.getIdentifier())
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .cautionMessage(requestDto.getCautionMessage())
                .equipmentType(equipmentType)
                .status(requestDto.getStatus())
                .isPublic(requestDto.getIsPublic())
                .build();
        
        Equipment updatedEquipment = equipmentService.updateEquipment(id, equipment);
        EquipmentInfoDto equipmentInfoDto = convertToEquipmentInfoDto(updatedEquipment);
        
        return ResponseEntity.ok(equipmentInfoDto);
    }

    /**
     * 기자재 상태 변경 (관리자만)
     * @param id 기자재 ID
     * @param status 변경할 상태
     * @return 상태가 변경된 기자재 정보
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EquipmentInfoDto> changeEquipmentStatus(@PathVariable Long id, @RequestParam EquipmentStatus status) {
        log.info("기자재 상태 변경 요청 (관리자): ID={}, 상태={}", id, status);
        
        Equipment equipment = equipmentService.changeEquipmentStatus(id, status);
        EquipmentInfoDto equipmentInfoDto = convertToEquipmentInfoDto(equipment);
        
        return ResponseEntity.ok(equipmentInfoDto);
    }

    /**
     * 기자재 공개/비공개 설정 (관리자만)
     * @param id 기자재 ID
     * @param isPublic 공개 여부
     * @return 설정이 변경된 기자재 정보
     */
    @PatchMapping("/{id}/publicity")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EquipmentInfoDto> setEquipmentPublicity(@PathVariable Long id, @RequestParam Boolean isPublic) {
        log.info("기자재 공개 설정 변경 요청 (관리자): ID={}, 공개여부={}", id, isPublic);
        
        Equipment equipment = equipmentService.setEquipmentPublicity(id, isPublic);
        EquipmentInfoDto equipmentInfoDto = convertToEquipmentInfoDto(equipment);
        
        return ResponseEntity.ok(equipmentInfoDto);
    }

    /**
     * 기자재 삭제 (관리자만)
     * @param id 기자재 ID
     * @return 삭제 완료 응답
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        log.info("기자재 삭제 요청 (관리자): ID={}", id);
        
        equipmentService.deleteEquipment(id);
        
        return ResponseEntity.noContent().build();
    }

    /**
     * 공개된 기자재 목록 조회
     * @return 공개된 기자재 목록
     */
    @GetMapping("/public")
    public ResponseEntity<List<Equipment>> getPublicEquipments() {
        log.info("공개 기자재 목록 조회 요청");
        List<Equipment> publicEquipments = equipmentService.getPublicEquipments();
        return ResponseEntity.ok(publicEquipments);
    }

    /**
     * 사용 가능한 기자재 목록 조회
     * @return 사용 가능한 기자재 목록
     */
    @GetMapping("/available")
    public ResponseEntity<List<Equipment>> getAvailableEquipments() {
        log.info("사용 가능한 기자재 목록 조회 요청");
        List<Equipment> availableEquipments = equipmentService.getAvailableEquipments();
        return ResponseEntity.ok(availableEquipments);
    }

    /**
     * 특정 상태의 기자재 목록 조회
     * @param status 기자재 상태
     * @return 해당 상태의 기자재 목록
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Equipment>> getEquipmentsByStatus(@PathVariable EquipmentStatus status) {
        log.info("상태별 기자재 목록 조회 요청: 상태={}", status);
        List<Equipment> equipments = equipmentService.getEquipmentsByStatus(status);
        return ResponseEntity.ok(equipments);
    }

    /**
     * 특정 타입의 기자재 목록 조회
     * @param typeId 기자재 타입 ID
     * @return 해당 타입의 기자재 목록
     */
    @GetMapping("/type/{typeId}")
    public ResponseEntity<List<Equipment>> getEquipmentsByType(@PathVariable Long typeId) {
        log.info("타입별 기자재 목록 조회 요청: 타입ID={}", typeId);
        List<Equipment> equipments = equipmentService.getEquipmentsByType(typeId);
        return ResponseEntity.ok(equipments);
    }

    /**
     * 기자재 검색
     * @param keyword 검색 키워드
     * @return 검색 결과
     */
    @GetMapping("/search")
    public ResponseEntity<List<Equipment>> searchEquipments(@RequestParam String keyword) {
        log.info("기자재 검색 요청: 키워드={}", keyword);
        List<Equipment> searchResults = equipmentService.searchEquipments(keyword);
        return ResponseEntity.ok(searchResults);
    }

    /**
     * 기자재 통계 정보 조회
     * @return 기자재 통계 정보
     */
    @GetMapping("/statistics")
    public ResponseEntity<EquipmentStatisticsDto> getEquipmentStatistics() {
        log.info("기자재 통계 조회 요청");
        EquipmentStatisticsDto statistics = equipmentService.getEquipmentStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * 기자재 타입별 통계 조회
     * @return 타입별 기자재 수
     */
    @GetMapping("/statistics/by-type")
    public ResponseEntity<List<Object[]>> getEquipmentCountByType() {
        log.info("기자재 타입별 통계 조회 요청");
        List<Object[]> typeStatistics = equipmentService.getEquipmentCountByType();
        return ResponseEntity.ok(typeStatistics);
    }

    /**
     * 기자재 버전 히스토리 조회
     * @param id 기자재 ID
     * @return 버전 히스토리
     */
    @GetMapping("/{id}/history")
    public ResponseEntity<EquipmentVersionHistoryDto> getEquipmentVersionHistory(@PathVariable Long id) {
        log.info("기자재 버전 히스토리 조회 요청: ID={}", id);
        EquipmentVersionHistoryDto history = equipmentService.getEquipmentVersionHistory(id);
        return ResponseEntity.ok(history);
    }

    /**
     * Equipment 엔티티를 EquipmentInfoDto로 변환
     * @param equipment Equipment 엔티티
     * @return EquipmentInfoDto
     */
    private EquipmentInfoDto convertToEquipmentInfoDto(Equipment equipment) {
        return EquipmentInfoDto.builder()
                .equipmentId(equipment.getId())
                .name(equipment.getName())
                .cautionMessage(equipment.getCautionMessage())
                .type(equipment.getTypeName())
                .createdAt(equipment.getCreatedAt())
                .lastUpdateTime(equipment.getLastUpdateTime())
                .description(equipment.getDescription())
                .status(equipment.getStatus().getMessage())
                .identifier(equipment.getIdentifier())
                .build();
    }
}
