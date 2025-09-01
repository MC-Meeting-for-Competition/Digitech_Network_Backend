package kr.hs.sdh.digitechnetwork.service;

import kr.hs.sdh.digitechnetwork.dto.EquipmentInfoDto;
import kr.hs.sdh.digitechnetwork.entity.Equipment;
import kr.hs.sdh.digitechnetwork.enums.EquipmentStatus;
import kr.hs.sdh.digitechnetwork.exception.DuplicateResourceException;
import kr.hs.sdh.digitechnetwork.exception.ResourceNotFoundException;
import kr.hs.sdh.digitechnetwork.repository.EquipmentRepository;
import kr.hs.sdh.digitechnetwork.repository.EquipmentTypeRepository;
import kr.hs.sdh.digitechnetwork.dto.EquipmentStatisticsDto;
import kr.hs.sdh.digitechnetwork.dto.EquipmentVersionHistoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Equipment 서비스 구현체
 * EquipmentService 인터페이스의 실제 구현
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentTypeRepository equipmentTypeRepository;

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Equipment registerEquipment(Equipment equipment) {
        log.info("기자재 등록 시작 (관리자): {}", equipment.getIdentifier());
        
        // 식별자 중복 검사
        if (equipmentRepository.existsByIdentifier(equipment.getIdentifier())) {
            throw new DuplicateResourceException("Equipment", equipment.getIdentifier());
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
        log.info("기자재 등록 완료 (관리자): ID={}, 식별자={}", savedEquipment.getId(), savedEquipment.getIdentifier());
        
        return savedEquipment;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Equipment updateEquipment(Long id, Equipment updatedEquipment) {
        log.info("기자재 수정 시작 (관리자): ID={}", id);
        
        Equipment existingEquipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", id));
        
        // 식별자 변경 시 중복 검사
        if (!existingEquipment.getIdentifier().equals(updatedEquipment.getIdentifier()) &&
            equipmentRepository.existsByIdentifier(updatedEquipment.getIdentifier())) {
            throw new DuplicateResourceException("Equipment", updatedEquipment.getIdentifier());
        }
        
        // 기자재 정보 업데이트
        existingEquipment.setIdentifier(updatedEquipment.getIdentifier());
        existingEquipment.setIsPublic(updatedEquipment.getIsPublic());
        existingEquipment.setStatus(updatedEquipment.getStatus());
        existingEquipment.setCautionMessage(updatedEquipment.getCautionMessage());
        existingEquipment.setEquipmentType(updatedEquipment.getEquipmentType());
        
        Equipment savedEquipment = equipmentRepository.save(existingEquipment);
        log.info("기자재 수정 완료 (관리자): ID={}, 식별자={}", savedEquipment.getId(), savedEquipment.getIdentifier());
        
        return savedEquipment;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Equipment changeEquipmentStatus(Long id, EquipmentStatus status) {
        log.info("기자재 상태 변경 시작 (관리자): ID={}, 상태={}", id, status);
        
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", id));
        
        equipment.setStatus(status);
        Equipment savedEquipment = equipmentRepository.save(equipment);
        
        log.info("기자재 상태 변경 완료 (관리자): ID={}, 상태={}", savedEquipment.getId(), savedEquipment.getStatus());
        return savedEquipment;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Equipment setEquipmentPublicity(Long id, Boolean isPublic) {
        log.info("기자재 공개 설정 변경 시작 (관리자): ID={}, 공개여부={}", id, isPublic);
        
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", id));
        
        equipment.setIsPublic(isPublic);
        Equipment savedEquipment = equipmentRepository.save(equipment);
        
        log.info("기자재 공개 설정 변경 완료 (관리자): ID={}, 공개여부={}", savedEquipment.getId(), savedEquipment.getIsPublic());
        return savedEquipment;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEquipment(Long id) {
        log.info("기자재 삭제 시작 (관리자): ID={}", id);
        
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", id));
        
        // 논리적 삭제: 상태를 UNAVAILABLE로 변경
        equipment.setStatus(EquipmentStatus.UNAVAILABLE);
        equipment.setIsPublic(false);
        equipmentRepository.save(equipment);
        
        log.info("기자재 삭제 완료 (관리자): ID={}", id);
    }

    @Override
    public Optional<Equipment> getEquipmentById(Long id) {
        return equipmentRepository.getEquipmentById(id);
    }

    @Override
    public Optional<Equipment> getEquipmentByIdentifier(String identifier) {
        return equipmentRepository.findByIdentifier(identifier);
    }

    @Override
    public List<EquipmentInfoDto> getAllEquipments() {
        List<Equipment> equipmentList = equipmentRepository.findAll();
        return convertToEquipmentInfoDtoList(equipmentList);
    }

    @Override
    public List<Equipment> getPublicEquipments() {
        return equipmentRepository.findByIsPublicTrue();
    }

    @Override
    public List<Equipment> getEquipmentsByStatus(EquipmentStatus status) {
        return equipmentRepository.findByStatus(status);
    }

    @Override
    public List<Equipment> getEquipmentsByType(Long typeId) {
        return equipmentRepository.findByEquipmentTypeId(typeId);
    }

    @Override
    public List<Equipment> getAvailableEquipments() {
        return equipmentRepository.findByStatusAndIsPublicTrue(EquipmentStatus.AVAILABLE);
    }

    @Override
    public EquipmentStatisticsDto getEquipmentStatistics() {
        long totalCount = equipmentRepository.count();
        long availableCount = equipmentRepository.countByStatus(EquipmentStatus.AVAILABLE);
        long rentedCount = equipmentRepository.countByStatus(EquipmentStatus.RENT);
        long brokenCount = equipmentRepository.countByStatus(EquipmentStatus.BROKEN);
        long publicCount = equipmentRepository.countByIsPublicTrue();
        
        return EquipmentStatisticsDto.builder()
                .totalCount(totalCount)
                .availableCount(availableCount)
                .rentedCount(rentedCount)
                .brokenCount(brokenCount)
                .publicCount(publicCount)
                .build();
    }

    @Override
    public List<Equipment> searchEquipments(String keyword) {
        return equipmentRepository.findByIdentifierContainingOrCautionMessageContaining(keyword, keyword);
    }

    @Override
    public List<Object[]> getEquipmentCountByType() {
        return equipmentTypeRepository.findPublicTypesWithEquipmentCount();
    }

    @Override
    public EquipmentVersionHistoryDto getEquipmentVersionHistory(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", id));
        
        return EquipmentVersionHistoryDto.builder()
                .equipmentId(equipment.getId())
                .identifier(equipment.getIdentifier())
                .createdAt(equipment.getCreatedAt())
                .lastUpdateTime(equipment.getLastUpdateTime())
                .currentStatus(equipment.getStatus())
                .currentPublicity(equipment.getIsPublic())
                .build();
    }

    /**
     * Equipment 엔티티 리스트를 EquipmentInfoDto 리스트로 변환
     * @param equipmentList Equipment 엔티티 리스트
     * @return EquipmentInfoDto 리스트
     */
    private List<EquipmentInfoDto> convertToEquipmentInfoDtoList(List<Equipment> equipmentList) {
        return equipmentList.stream()
                .map(this::convertToEquipmentInfoDto)
                .collect(Collectors.toList());
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
