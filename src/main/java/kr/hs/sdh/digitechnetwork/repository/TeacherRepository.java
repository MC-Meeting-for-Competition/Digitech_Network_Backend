package kr.hs.sdh.digitechnetwork.repository;

import kr.hs.sdh.digitechnetwork.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    // 이름으로 교사 찾기
    List<Teacher> findByNameContaining(String name);
    
    // 이메일로 교사 찾기
    Optional<Teacher> findByEmail(String email);
    
    // 전화번호로 교사 찾기
    Optional<Teacher> findByPhoneNumber(String phoneNumber);
    
    // 활성화된 교사만 찾기
    @Query("SELECT t FROM Teacher t WHERE t.isEnabled = true")
    List<Teacher> findActiveTeachers();
    
    // 특정 교사의 대여 이력 수 조회
    @Query("SELECT COUNT(trh) FROM TeacherRentHistory trh WHERE trh.teacher.id = :teacherId")
    long countRentHistoryByTeacherId(@org.springframework.data.repository.query.Param("teacherId") Long teacherId);
}
