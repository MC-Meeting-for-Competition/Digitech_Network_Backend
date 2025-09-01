package kr.hs.sdh.digitechnetwork.service;

import kr.hs.sdh.digitechnetwork.entity.Equipment;
import kr.hs.sdh.digitechnetwork.enums.EquipmentStatus;
import kr.hs.sdh.digitechnetwork.dto.EquipmentStatisticsDto;
import kr.hs.sdh.digitechnetwork.dto.EquipmentVersionHistoryDto;

import java.util.List;
import java.util.Optional;

/**
 * Equipment 서비스 인터페이스
 * 기자재의 CRUD 를 담당
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
public interface EquipmentService {
    
    /**
     * 기자재 등록
     * @param equipment 등록할 기자재 정보
     * @return 등록된 기자재
     */
    Equipment registerEquipment(Equipment equipment);
    
    /**
     * 기자재 정보 수정
     * @param id 기자재 ID
     * @param updatedEquipment 수정할 기자재 정보
     * @return 수정된 기자재
     */
    Equipment updateEquipment(Long id, Equipment updatedEquipment);
    
    /**
     * 기자재 상태 변경
     * @param id 기자재 ID
     * @param status 변경할 상태
     * @return 상태가 변경된 기자재
     */
    Equipment changeEquipmentStatus(Long id, EquipmentStatus status);
    
    /**
     * 기자재 공개/비공개 설정
     * @param id 기자재 ID
     * @param isPublic 공개 여부
     * @return 설정이 변경된 기자재
     */
    Equipment setEquipmentPublicity(Long id, Boolean isPublic);
    
    /**
     * 기자재 삭제 (논리적 삭제)
     * @param id 기자재 ID
     */
    void deleteEquipment(Long id);
    
    /**
     * 기자재 조회 (ID로)
     * @param id 기자재 ID
     * @return 기자재 정보 (EquipmentType 포함)
     */
    Optional<Equipment> getEquipmentById(Long id);
    
    /**
     * 기자재 조회 (식별자로)
     * @param identifier 기자재 식별자
     * @return 기자재 정보
     */
    Optional<Equipment> getEquipmentByIdentifier(String identifier);
    
    /**
     * 모든 기자재 조회
     * @return 기자재 목록
     */
    List<Equipment> getAllEquipments();
    
    /**
     * 공개된 기자재만 조회
     * @return 공개된 기자재 목록
     */
    List<Equipment> getPublicEquipments();
    
    /**
     * 특정 상태의 기자재 조회
     * @param status 기자재 상태
     * @return 해당 상태의 기자재 목록
     */
    List<Equipment> getEquipmentsByStatus(EquipmentStatus status);
    
    /**
     * 특정 타입의 기자재 조회
     * @param typeId 기자재 타입 ID
     * @return 해당 타입의 기자재 목록
     */
    List<Equipment> getEquipmentsByType(Long typeId);
    
    /**
     * 사용 가능한 기자재 조회
     * @return 사용 가능한 기자재 목록
     */
    List<Equipment> getAvailableEquipments();
    
    /**
     * 기자재 통계 정보 조회
     * @return 기자재 통계 정보
     */
    EquipmentStatisticsDto getEquipmentStatistics();
    
    /**
     * 기자재 검색
     * @param keyword 검색 키워드
     * @return 검색 결과
     */
    List<Equipment> searchEquipments(String keyword);
    
    /**
     * 기자재 타입별 통계
     * @return 타입별 기자재 수
     */
    List<Object[]> getEquipmentCountByType();
    
    /**
     * 기자재 버전 히스토리 조회 (생성/수정 이력)
     * @param id 기자재 ID
     * @return 버전 히스토리
     */
    EquipmentVersionHistoryDto getEquipmentVersionHistory(Long id);
}
