package kr.hs.sdh.digitechnetwork.repository;

import kr.hs.sdh.digitechnetwork.entity.EquipmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * EquipmentType Repository
 * 기자재 타입(EquipmentType) 엔티티에 대한 데이터 접근 계층
 *
 * 주요 기능:
 * - 기자재 타입 기본 CRUD 작업
 * - 기자재 타입별 분류 및 관리
 * - 공개/비공개 기자재 타입 조회
 * - 타입별 기자재 통계 정보 조회
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Repository
public interface EquipmentTypeRepository extends JpaRepository<EquipmentType, Long> {

    /**
     * 타입명으로 찾기
     * 기자재 타입의 고유 이름으로 조회
     *
     * @param type 기자재 타입명
     * @return 해당 타입명의 기자재 타입 (Optional)
     */
    Optional<EquipmentType> findByType(String type);

    /**
     * 타입명으로 존재 여부 확인
     * 기자재 타입 등록 시 중복 검사에 사용
     *
     * @param type 기자재 타입명
     * @return 존재 여부 (true: 존재, false: 존재하지 않음)
     */
    boolean existsByType(String type);

    /**
     * 공개된 타입만 찾기
     * 사용자에게 보여질 수 있는 기자재 타입만 조회
     *
     * @return 공개된 기자재 타입 목록
     */
    List<EquipmentType> findByIsPublicTrue();

    /**
     * 타입명으로 검색 (부분 일치)
     * 기자재 타입명 기반 부분 검색
     *
     * @param type 기자재 타입명 (부분 일치)
     * @return 타입명이 포함된 기자재 타입 목록
     */
    List<EquipmentType> findByTypeContaining(String type);

    /**
     * 특정 타입의 기자재 수 조회
     * 기자재 타입별 통계 정보 생성에 사용
     *
     * @param typeId 기자재 타입 ID
     * @return 해당 타입의 기자재 개수
     */
    @Query("SELECT COUNT(e) FROM Equipment e WHERE e.equipmentType.id = :typeId")
    long countEquipmentByTypeId(@Param("typeId") Long typeId);

    /**
     * 공개된 타입의 기자재 수와 함께 조회
     * 공개된 타입별로 소속된 기자재 개수를 함께 조회
     *
     * @return 타입별 기자재 수 정보 (타입 정보와 개수)
     */
    @Query("SELECT et, COUNT(e) FROM EquipmentType et LEFT JOIN et.equipmentList e " +
            "WHERE et.isPublic = true GROUP BY et")
    List<Object[]> findPublicTypesWithEquipmentCount();

    /**
     * 공개된 타입과 해당 타입의 기자재 개수를 별도로 조회
     * 더 간단한 방식으로 타입별 기자재 개수 조회
     *
     * @return 타입별 기자재 개수 정보
     */
    @Query("SELECT et.type, COUNT(e) FROM EquipmentType et LEFT JOIN et.equipmentList e " +
            "WHERE et.isPublic = true GROUP BY et.type, et.id")
    List<Object[]> findPublicTypesWithEquipmentCountSimple();

    /**
     * 모든 타입별 기자재 개수 조회
     * 전체 통계 정보 생성에 사용
     *
     * @return 모든 타입별 기자재 개수
     */
    @Query("SELECT et.type, COUNT(e) FROM EquipmentType et LEFT JOIN et.equipmentList e " +
            "GROUP BY et.type, et.id ORDER BY COUNT(e) DESC")
    List<Object[]> findAllTypesWithEquipmentCount();
}