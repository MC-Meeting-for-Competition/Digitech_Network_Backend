package kr.hs.sdh.digitechnetwork.repository;

import kr.hs.sdh.digitechnetwork.entity.StudentRentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * StudentRentHistory Repository
 * 학생 대여 이력(StudentRentHistory) 엔티티에 대한 데이터 접근 계층
 * 
 * 주요 기능:
 * - 학생 대여 이력 기본 CRUD 작업
 * - 학생별 대여 이력 조회 및 관리
 * - 대여 이력별 학생 정보 조회
 * - 기간별 대여 이력 통계 및 조회
 * 
 * 이 Repository는 Student와 RentHistory 간의 중간 테이블을 관리하며,
 * 학생의 기자재 대여 이력을 추적하고 통계 정보를 제공합니다.
 * 
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Repository
public interface StudentRentHistoryRepository extends JpaRepository<StudentRentHistory, Long> {
    
    /**
     * 특정 학생의 대여 이력 조회
     * 학생 ID를 통해 해당 학생의 모든 대여 이력 조회
     * 
     * @param studentId 학생 ID
     * @return 해당 학생의 대여 이력 목록
     */
    List<StudentRentHistory> findByStudentId(Long studentId);
    
    /**
     * 특정 대여 이력의 학생 대여 이력 조회
     * 대여 이력 ID를 통해 해당 대여에 참여한 학생들의 정보 조회
     * 
     * @param rentHistoryId 대여 이력 ID
     * @return 해당 대여 이력의 학생 대여 정보 목록
     */
    List<StudentRentHistory> findByRentHistoryId(Long rentHistoryId);
    
    /**
     * 특정 학생의 특정 대여 이력 조회
     * 학생과 대여 이력을 모두 만족하는 대여 정보 조회
     * 
     * @param studentId 학생 ID
     * @param rentHistoryId 대여 이력 ID
     * @return 해당 조건의 학생 대여 이력 (Optional)
     */
    Optional<StudentRentHistory> findByStudentIdAndRentHistoryId(Long studentId, Long rentHistoryId);
    
    /**
     * 학생별 대여 이력 수 조회
     * 특정 학생의 총 대여 횟수 통계 정보 생성에 사용
     * 
     * @param studentId 학생 ID
     * @return 해당 학생의 대여 이력 수
     */
    @Query("SELECT COUNT(srh) FROM StudentRentHistory srh WHERE srh.student.id = :studentId")
    long countByStudentId(@Param("studentId") Long studentId);
    
    /**
     * 특정 대여 이력의 학생 수 조회
     * 특정 대여에 참여한 학생 수 통계 정보 생성에 사용
     * 
     * @param rentHistoryId 대여 이력 ID
     * @return 해당 대여 이력에 참여한 학생 수
     */
    @Query("SELECT COUNT(srh) FROM StudentRentHistory srh WHERE srh.rentHistory.id = :rentHistoryId")
    long countByRentHistoryId(@Param("rentHistoryId") Long rentHistoryId);
    
    /**
     * 특정 기간의 학생 대여 이력 조회
     * 날짜 범위를 지정하여 학생 대여 이력 조회
     * 
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 해당 기간의 학생 대여 이력 목록 (최신순 정렬)
     */
    @Query("SELECT srh FROM StudentRentHistory srh WHERE srh.createdAt BETWEEN :startDate AND :endDate ORDER BY srh.createdAt DESC")
    List<StudentRentHistory> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * 특정 학생의 특정 기간 대여 이력 조회
     * 학생과 기간을 모두 만족하는 대여 이력 조회
     * 
     * @param studentId 학생 ID
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 해당 조건의 학생 대여 이력 목록 (최신순 정렬)
     */
    @Query("SELECT srh FROM StudentRentHistory srh WHERE srh.student.id = :studentId AND srh.createdAt BETWEEN :startDate AND :endDate ORDER BY srh.createdAt DESC")
    List<StudentRentHistory> findByStudentIdAndDateRange(@Param("studentId") Long studentId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
