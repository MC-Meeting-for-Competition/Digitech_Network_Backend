package kr.hs.sdh.digitechnetwork.repository;

import kr.hs.sdh.digitechnetwork.entity.RentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentHistoryRepository extends JpaRepository<RentHistory, Long> {
    
    // 특정 기자재의 대여 이력 조회
    @Query("SELECT rh FROM RentHistory rh " +
           "JOIN rh.studentRentHistory srh " +
           "WHERE srh.rentHistory.id = :equipmentId " +
           "ORDER BY rh.createdAt DESC")
    List<RentHistory> findByEquipmentId(@Param("equipmentId") Long equipmentId);
    
    // 특정 학생의 대여 이력 조회
    @Query("SELECT rh FROM RentHistory rh " +
           "JOIN rh.studentRentHistory srh " +
           "WHERE srh.student.id = :studentId " +
           "ORDER BY rh.createdAt DESC")
    List<RentHistory> findByStudentId(@Param("studentId") Long studentId);
    
    // 특정 교사의 대여 이력 조회
    @Query("SELECT rh FROM RentHistory rh " +
           "JOIN rh.teacherRentHistory trh " +
           "WHERE trh.teacher.id = :teacherId " +
           "ORDER BY rh.createdAt DESC")
    List<RentHistory> findByTeacherId(@Param("teacherId") Long teacherId);
    
    // 특정 기간의 대여 이력 조회
    @Query("SELECT rh FROM RentHistory rh " +
           "WHERE rh.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY rh.createdAt DESC")
    List<RentHistory> findByDateRange(
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate);
    
    // 최근 대여 이력 조회 (최신순)
    @Query("SELECT rh FROM RentHistory rh ORDER BY rh.createdAt DESC")
    List<RentHistory> findRecentRentHistories();
}
