package kr.hs.sdh.digitechnetwork.repository;

import kr.hs.sdh.digitechnetwork.entity.Equipment;
import kr.hs.sdh.digitechnetwork.enums.EquipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Equipment Repository
 * 기자재(Equipment) 엔티티에 대한 데이터 접근 계층
 * 
 * 주요 기능:
 * - 기자재 CRUD 작업
 * - 기자재 상태별 조회 및 관리
 * - 기자재 검색 및 필터링
 * - 기자재 통계 정보 조회
 * 
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    
    /**
     * 기자재를 ID로 조회 (EquipmentType 정보 포함)
     * JOIN FETCH를 사용하여 N+1 문제 방지
     * 
     * @param id 기자재 ID
     * @return EquipmentType 정보가 포함된 기자재 (Optional)
     */
    @Query("SELECT e FROM Equipment e JOIN FETCH e.equipmentType WHERE e.id = :id")
    Optional<Equipment> getEquipmentById(Long id);
    
    /**
     * 식별자로 기자재 찾기
     * 기자재의 고유 식별자를 통해 조회
     * 
     * @param identifier 기자재 식별자
     * @return 기자재 정보 (Optional)
     */
    Optional<Equipment> findByIdentifier(String identifier);
    
    /**
     * 식별자 존재 여부 확인
     * 기자재 등록 시 중복 검사에 사용
     * 
     * @param identifier 기자재 식별자
     * @return 존재 여부 (true: 존재, false: 존재하지 않음)
     */
    boolean existsByIdentifier(String identifier);
    
    /**
     * 공개된 기자재만 찾기
     * 사용자에게 보여질 수 있는 기자재 목록 조회
     * 
     * @return 공개된 기자재 목록
     */
    List<Equipment> findByIsPublicTrue();
    
    /**
     * 특정 상태의 기자재 찾기
     * 기자재의 현재 상태에 따른 분류 조회
     * 
     * @param status 기자재 상태 (AVAILABLE, RENT, BROKEN, CHECK, UNAVAILABLE, FIX)
     * @return 해당 상태의 기자재 목록
     */
    List<Equipment> findByStatus(EquipmentStatus status);
    
    /**
     * 특정 타입의 기자재 찾기
     * 기자재 분류에 따른 조회
     * 
     * @param typeId 기자재 타입 ID
     * @return 해당 타입의 기자재 목록
     */
    List<Equipment> findByEquipmentTypeId(Long typeId);
    
    /**
     * 상태와 공개 여부로 기자재 찾기
     * 특정 상태이면서 공개된 기자재만 조회
     * 
     * @param status 기자재 상태
     * @return 해당 상태이면서 공개된 기자재 목록
     */
    List<Equipment> findByStatusAndIsPublicTrue(EquipmentStatus status);
    
    /**
     * 식별자나 주의사항으로 검색
     * 키워드 기반의 기자재 검색 기능
     * 
     * @param identifier 기자재 식별자 (부분 일치)
     * @param cautionMessage 주의사항 메시지 (부분 일치)
     * @return 검색 조건을 만족하는 기자재 목록
     */
    List<Equipment> findByIdentifierContainingOrCautionMessageContaining(String identifier, String cautionMessage);
    
    /**
     * 특정 상태의 기자재 수 조회
     * 기자재 상태별 통계 정보 생성에 사용
     * 
     * @param status 기자재 상태
     * @return 해당 상태의 기자재 개수
     */
    long countByStatus(EquipmentStatus status);
    
    /**
     * 공개된 기자재 수 조회
     * 공개 기자재 통계 정보 생성에 사용
     * 
     * @return 공개된 기자재 개수
     */
    long countByIsPublicTrue();
}
