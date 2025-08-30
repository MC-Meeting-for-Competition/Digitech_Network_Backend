package kr.hs.sdh.digitechnetwork.repository;

import kr.hs.sdh.digitechnetwork.entity.StudentRentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRentHistoryRepository extends JpaRepository<StudentRentHistory, Long> {
    
    // 특정 학생의 대여 이력 조회
    List<StudentRentHistory> findByStudentId(Long studentId);
    
    // 특정 대여 이력의 학생 대여 이력 조회
    List<StudentRentHistory> findByRentHistoryId(Long rentHistoryId);
    
    // 특정 학생의 특정 대여 이력 조회
    Optional<StudentRentHistory> findByStudentIdAndRentHistoryId(Long studentId, Long rentHistoryId);
    
    // 학생별 대여 이력 수 조회
    @Query("SELECT COUNT(srh) FROM StudentRentHistory srh WHERE srh.student.id = :studentId")
    long countByStudentId(@Param("studentId") Long studentId);
    
    // 특정 대여 이력의 학생 수 조회
    @Query("SELECT COUNT(srh) FROM StudentRentHistory srh WHERE srh.rentHistory.id = :rentHistoryId")
    long countByRentHistoryId(@Param("rentHistoryId") Long rentHistoryId);
}
