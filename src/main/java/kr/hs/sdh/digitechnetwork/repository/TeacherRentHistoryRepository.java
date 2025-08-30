package kr.hs.sdh.digitechnetwork.repository;

import kr.hs.sdh.digitechnetwork.entity.TeacherRentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRentHistoryRepository extends JpaRepository<TeacherRentHistory, Long> {
    
    // 특정 교사의 대여 이력 조회
    List<TeacherRentHistory> findByTeacherId(Long teacherId);
    
    // 특정 대여 이력의 교사 대여 이력 조회
    List<TeacherRentHistory> findByRentHistoryId(Long rentHistoryId);
    
    // 특정 교사의 특정 대여 이력 조회
    Optional<TeacherRentHistory> findByTeacherIdAndRentHistoryId(Long teacherId, Long rentHistoryId);
    
    // 교사별 대여 이력 수 조회
    @Query("SELECT COUNT(trh) FROM TeacherRentHistory trh WHERE trh.teacher.id = :teacherId")
    long countByTeacherId(@Param("teacherId") Long teacherId);
    
    // 특정 대여 이력의 교사 수 조회
    @Query("SELECT COUNT(trh) FROM TeacherRentHistory trh WHERE trh.rentHistory.id = :rentHistoryId")
    long countByRentHistoryId(@Param("rentHistoryId") Long rentHistoryId);
    
    // 특정 기간의 교사 대여 이력 조회
    @Query("SELECT trh FROM TeacherRentHistory trh WHERE trh.createdAt BETWEEN :startDate AND :endDate ORDER BY trh.createdAt DESC")
    List<TeacherRentHistory> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // 특정 교사의 특정 기간 대여 이력 조회
    @Query("SELECT trh FROM TeacherRentHistory trh WHERE trh.teacher.id = :teacherId AND trh.createdAt BETWEEN :startDate AND :endDate ORDER BY trh.createdAt DESC")
    List<TeacherRentHistory> findByTeacherIdAndDateRange(@Param("teacherId") Long teacherId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
