package kr.hs.sdh.digitechnetwork.service;

import kr.hs.sdh.digitechnetwork.entity.Equipment;
import kr.hs.sdh.digitechnetwork.entity.EquipmentType;
import kr.hs.sdh.digitechnetwork.enums.EquipmentStatus;
import kr.hs.sdh.digitechnetwork.repository.EquipmentRepository;
import kr.hs.sdh.digitechnetwork.repository.EquipmentTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Builder;

/**
 * Equipment 서비스
 * 기자재의 CRUD 및 버전 관리를 담당
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentTypeRepository equipmentTypeRepository;

    /**
     * 기자재 등록
     * @param equipment 등록할 기자재 정보
     * @return 등록된 기자재
     */
    @Transactional
    public Equipment registerEquipment(Equipment equipment) {
        log.info("기자재 등록 시작: {}", equipment.getIdentifier());
        
        // 식별자 중복 검사
        if (equipmentRepository.existsByIdentifier(equipment.getIdentifier())) {
            throw new IllegalArgumentException("이미 존재하는 식별자입니다: " + equipment.getIdentifier());
        }
        
        // 기본 상태 설정
        if (equipment.getStatus() == null) {
            equipment.setStatus(EquipmentStatus.AVAILABLE);
        }
        
        // 공개 여부 기본값 설정
        if (equipment.getIsPublic() == null) {
            equipment.setIsPublic(true);
        }
        
        Equipment savedEquipment = equipmentRepository.save(equipment);
        log.info("기자재 등록 완료: ID={}, 식별자={}", savedEquipment.getId(), savedEquipment.getIdentifier());
        
        return savedEquipment;
    }

    /**
     * 기자재 정보 수정
     * @param id 기자재 ID
     * @param updatedEquipment 수정할 기자재 정보
     * @return 수정된 기자재
     */
    @Transactional
    public Equipment updateEquipment(Long id, Equipment updatedEquipment) {
        log.info("기자재 수정 시작: ID={}", id);
        
        Equipment existingEquipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기자재입니다: " + id));
        
        // 식별자 변경 시 중복 검사
        if (!existingEquipment.getIdentifier().equals(updatedEquipment.getIdentifier()) &&
            equipmentRepository.existsByIdentifier(updatedEquipment.getIdentifier())) {
            throw new IllegalArgumentException("이미 존재하는 식별자입니다: " + updatedEquipment.getIdentifier());
        }
        
        // 기자재 정보 업데이트
        existingEquipment.setIdentifier(updatedEquipment.getIdentifier());
        existingEquipment.setIsPublic(updatedEquipment.getIsPublic());
        existingEquipment.setStatus(updatedEquipment.getStatus());
        existingEquipment.setCautionMessage(updatedEquipment.getCautionMessage());
        existingEquipment.setEquipmentType(updatedEquipment.getEquipmentType());
        
        Equipment savedEquipment = equipmentRepository.save(existingEquipment);
        log.info("기자재 수정 완료: ID={}, 식별자={}", savedEquipment.getId(), savedEquipment.getIdentifier());
        
        return savedEquipment;
    }

    /**
     * 기자재 상태 변경
     * @param id 기자재 ID
     * @param status 변경할 상태
     * @return 상태가 변경된 기자재
     */
    @Transactional
    public Equipment changeEquipmentStatus(Long id, EquipmentStatus status) {
        log.info("기자재 상태 변경 시작: ID={}, 상태={}", id, status);
        
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기자재입니다: " + id));
        
        equipment.setStatus(status);
        Equipment savedEquipment = equipmentRepository.save(equipment);
        
        log.info("기자재 상태 변경 완료: ID={}, 상태={}", savedEquipment.getId(), savedEquipment.getStatus());
        return savedEquipment;
    }

    /**
     * 기자재 공개/비공개 설정
     * @param id 기자재 ID
     * @param isPublic 공개 여부
     * @return 설정이 변경된 기자재
     */
    @Transactional
    public Equipment setEquipmentPublicity(Long id, Boolean isPublic) {
        log.info("기자재 공개 설정 변경 시작: ID={}, 공개여부={}", id, isPublic);
        
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기자재입니다: " + id));
        
        equipment.setIsPublic(isPublic);
        Equipment savedEquipment = equipmentRepository.save(equipment);
        
        log.info("기자재 공개 설정 변경 완료: ID={}, 공개여부={}", savedEquipment.getId(), savedEquipment.getIsPublic());
        return savedEquipment;
    }

    /**
     * 기자재 삭제 (논리적 삭제)
     * @param id 기자재 ID
     */
    @Transactional
    public void deleteEquipment(Long id) {
        log.info("기자재 삭제 시작: ID={}", id);
        
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기자재입니다: " + id));
        
        // 논리적 삭제: 상태를 UNAVAILABLE로 변경
        equipment.setStatus(EquipmentStatus.UNAVAILABLE);
        equipment.setIsPublic(false);
        equipmentRepository.save(equipment);
        
        log.info("기자재 삭제 완료: ID={}", id);
    }

    /**
     * 기자재 조회 (ID로)
     * @param id 기자재 ID
     * @return 기자재 정보 (EquipmentType 포함)
     */
    public Optional<Equipment> getEquipmentById(Long id) {
        return equipmentRepository.getEquipmentById(id);
    }

    /**
     * 기자재 조회 (식별자로)
     * @param identifier 기자재 식별자
     * @return 기자재 정보
     */
    public Optional<Equipment> getEquipmentByIdentifier(String identifier) {
        return equipmentRepository.findByIdentifier(identifier);
    }

    /**
     * 모든 기자재 조회
     * @return 기자재 목록
     */
    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

    /**
     * 공개된 기자재만 조회
     * @return 공개된 기자재 목록
     */
    public List<Equipment> getPublicEquipments() {
        return equipmentRepository.findByIsPublicTrue();
    }

    /**
     * 특정 상태의 기자재 조회
     * @param status 기자재 상태
     * @return 해당 상태의 기자재 목록
     */
    public List<Equipment> getEquipmentsByStatus(EquipmentStatus status) {
        return equipmentRepository.findByStatus(status);
    }

    /**
     * 특정 타입의 기자재 조회
     * @param typeId 기자재 타입 ID
     * @return 해당 타입의 기자재 목록
     */
    public List<Equipment> getEquipmentsByType(Long typeId) {
        return equipmentRepository.findByEquipmentTypeId(typeId);
    }

    /**
     * 사용 가능한 기자재 조회
     * @return 사용 가능한 기자재 목록
     */
    public List<Equipment> getAvailableEquipments() {
        return equipmentRepository.findByStatusAndIsPublicTrue(EquipmentStatus.AVAILABLE);
    }

    /**
     * 기자재 통계 정보 조회
     * @return 기자재 통계 정보
     */
    public EquipmentStatistics getEquipmentStatistics() {
        long totalCount = equipmentRepository.count();
        long availableCount = equipmentRepository.countByStatus(EquipmentStatus.AVAILABLE);
        long rentedCount = equipmentRepository.countByStatus(EquipmentStatus.RENT);
        long brokenCount = equipmentRepository.countByStatus(EquipmentStatus.BROKEN);
        long publicCount = equipmentRepository.countByIsPublicTrue();
        
        return EquipmentStatistics.builder()
                .totalCount(totalCount)
                .availableCount(availableCount)
                .rentedCount(rentedCount)
                .brokenCount(brokenCount)
                .publicCount(publicCount)
                .build();
    }

    /**
     * 기자재 검색
     * @param keyword 검색 키워드
     * @return 검색 결과
     */
    public List<Equipment> searchEquipments(String keyword) {
        return equipmentRepository.findByIdentifierContainingOrCautionMessageContaining(keyword, keyword);
    }

    /**
     * 기자재 타입별 통계
     * @return 타입별 기자재 수
     */
    public List<Object[]> getEquipmentCountByType() {
        return equipmentTypeRepository.findPublicTypesWithEquipmentCount();
    }

    /**
     * 기자재 버전 히스토리 조회 (생성/수정 이력)
     * @param id 기자재 ID
     * @return 버전 히스토리
     */
    public EquipmentVersionHistory getEquipmentVersionHistory(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기자재입니다: " + id));
        
        return EquipmentVersionHistory.builder()
                .equipmentId(equipment.getId())
                .identifier(equipment.getIdentifier())
                .createdAt(equipment.getCreatedAt())
                .lastUpdateTime(equipment.getLastUpdateTime())
                .currentStatus(equipment.getStatus())
                .currentPublicity(equipment.getIsPublic())
                .build();
    }

    /**
     * 기자재 통계 정보 DTO
     */
    @Getter
    @Builder
    public static class EquipmentStatistics {
        private final long totalCount;
        private final long availableCount;
        private final long rentedCount;
        private final long brokenCount;
        private final long publicCount;
    }

    /**
     * 기자재 버전 히스토리 DTO
     */
    @Getter
    @Builder
    public static class EquipmentVersionHistory {
        private final Long equipmentId;
        private final String identifier;
        private final LocalDateTime createdAt;
        private final LocalDateTime lastUpdateTime;
        private final EquipmentStatus currentStatus;
        private final Boolean currentPublicity;
    }
}
