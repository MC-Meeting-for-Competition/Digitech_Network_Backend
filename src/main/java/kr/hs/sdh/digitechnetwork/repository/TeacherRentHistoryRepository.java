package kr.hs.sdh.digitechnetwork.repository;

import kr.hs.sdh.digitechnetwork.entity.TeacherRentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * TeacherRentHistory Repository
 * 교사 대여 이력(TeacherRentHistory) 엔티티에 대한 데이터 접근 계층
 * 
 * 주요 기능:
 * - 교사 대여 이력 기본 CRUD 작업
 * - 교사별 대여 이력 조회 및 관리
 * - 대여 이력별 교사 정보 조회
 * - 기간별 대여 이력 통계 및 조회
 * 
 * 이 Repository는 Teacher와 RentHistory 간의 중간 테이블을 관리하며,
 * 교사의 기자재 대여 이력을 추적하고 통계 정보를 제공합니다.
 * 
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Repository
public interface TeacherRentHistoryRepository extends JpaRepository<TeacherRentHistory, Long> {
    
    /**
     * 특정 교사의 대여 이력 조회
     * 교사 ID를 통해 해당 교사의 모든 대여 이력 조회
     * 
     * @param teacherId 교사 ID
     * @return 해당 교사의 대여 이력 목록
     */
    List<TeacherRentHistory> findByTeacherId(Long teacherId);
    
    /**
     * 특정 대여 이력의 교사 대여 이력 조회
     * 대여 이력 ID를 통해 해당 대여에 참여한 교사들의 정보 조회
     * 
     * @param rentHistoryId 대여 이력 ID
     * @return 해당 대여 이력의 교사 대여 정보 목록
     */
    List<TeacherRentHistory> findByRentHistoryId(Long rentHistoryId);
    
    /**
     * 특정 교사의 특정 대여 이력 조회
     * 교사와 대여 이력을 모두 만족하는 대여 정보 조회
     * 
     * @param teacherId 교사 ID
     * @param rentHistoryId 대여 이력 ID
     * @return 해당 조건의 교사 대여 이력 (Optional)
     */
    Optional<TeacherRentHistory> findByTeacherIdAndRentHistoryId(Long teacherId, Long rentHistoryId);
    
    /**
     * 교사별 대여 이력 수 조회
     * 특정 교사의 총 대여 횟수 통계 정보 생성에 사용
     * 
     * @param teacherId 교사 ID
     * @return 해당 교사의 대여 이력 수
     */
    @Query("SELECT COUNT(trh) FROM TeacherRentHistory trh WHERE trh.teacher.id = :teacherId")
    long countByTeacherId(@Param("teacherId") Long teacherId);
    
    /**
     * 특정 대여 이력의 교사 수 조회
     * 특정 대여에 참여한 교사 수 통계 정보 생성에 사용
     * 
     * @param rentHistoryId 대여 이력 ID
     * @return 해당 대여 이력에 참여한 교사 수
     */
    @Query("SELECT COUNT(trh) FROM TeacherRentHistory trh WHERE trh.rentHistory.id = :rentHistoryId")
    long countByRentHistoryId(@Param("rentHistoryId") Long rentHistoryId);
    
    /**
     * 특정 기간의 교사 대여 이력 조회
     * 날짜 범위를 지정하여 교사 대여 이력 조회
     * 
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 해당 기간의 교사 대여 이력 목록 (최신순 정렬)
     */
    @Query("SELECT trh FROM TeacherRentHistory trh WHERE trh.createdAt BETWEEN :startDate AND :endDate ORDER BY trh.createdAt DESC")
    List<TeacherRentHistory> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * 특정 교사의 특정 기간 대여 이력 조회
     * 교사와 기간을 모두 만족하는 대여 이력 조회
     * 
     * @param teacherId 교사 ID
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 해당 조건의 교사 대여 이력 목록 (최신순 정렬)
     */
    @Query("SELECT trh FROM TeacherRentHistory trh WHERE trh.teacher.id = :teacherId AND trh.createdAt BETWEEN :startDate AND :endDate ORDER BY trh.createdAt DESC")
    List<TeacherRentHistory> findByTeacherIdAndDateRange(@Param("teacherId") Long teacherId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
