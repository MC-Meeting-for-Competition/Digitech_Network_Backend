package kr.hs.sdh.digitechnetwork.repository;

import kr.hs.sdh.digitechnetwork.entity.EquipmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentTypeRepository extends JpaRepository<EquipmentType, Long> {
    
    // 타입명으로 찾기
    Optional<EquipmentType> findByType(String type);
    
    // 타입명으로 존재 여부 확인
    boolean existsByType(String type);
    
    // 공개된 타입만 찾기
    List<EquipmentType> findByIsPublicTrue();
    
    // 타입명으로 검색 (부분 일치)
    List<EquipmentType> findByTypeContaining(String type);
    
    // 특정 타입의 기자재 수 조회
    @Query("SELECT COUNT(e) FROM Equipment e WHERE e.equipmentType.id = :typeId")
    long countEquipmentByTypeId(@Param("typeId") Long typeId);
    
    // 공개된 타입의 기자재 수와 함께 조회
    @Query("SELECT et, COUNT(e) FROM EquipmentType et LEFT JOIN et.equipmentList e " +
           "WHERE et.isPublic = true GROUP BY et")
    List<Object[]> findPublicTypesWithEquipmentCount();
}
