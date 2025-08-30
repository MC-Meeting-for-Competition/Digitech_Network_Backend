package kr.hs.sdh.digitechnetwork.repository;

import kr.hs.sdh.digitechnetwork.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    // User와의 관계를 통해 이메일로 교사 찾기
    @Query("SELECT t FROM Teacher t WHERE t.user.email = :email")
    Optional<Teacher> findByUserEmail(@Param("email") String email);
    
    // User와의 관계를 통해 이름으로 교사 찾기
    @Query("SELECT t FROM Teacher t WHERE t.user.name LIKE %:name%")
    List<Teacher> findByUserNameContaining(@Param("name") String name);
    
    // User와의 관계를 통해 전화번호로 교사 찾기
    @Query("SELECT t FROM Teacher t WHERE t.user.phoneNumber = :phoneNumber")
    Optional<Teacher> findByUserPhoneNumber(@Param("phoneNumber") String phoneNumber);
    
    // 활성화된 교사만 찾기
    @Query("SELECT t FROM Teacher t WHERE t.user.isEnabled = true")
    List<Teacher> findActiveTeachers();
    
    // 특정 교사의 대여 이력 수 조회
    @Query("SELECT COUNT(trh) FROM TeacherRentHistory trh WHERE trh.teacher.id = :teacherId")
    long countRentHistoryByTeacherId(@Param("teacherId") Long teacherId);
}
