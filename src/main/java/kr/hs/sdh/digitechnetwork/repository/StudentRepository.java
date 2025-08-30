package kr.hs.sdh.digitechnetwork.repository;

import kr.hs.sdh.digitechnetwork.entity.Student;
import kr.hs.sdh.digitechnetwork.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByGrade(Integer grade);
    List<Student> findByClassroom(Integer classroom);
    List<Student> findByGradeAndClassroom(Integer grade, Integer classroom);
    Optional<Student> findByStudentNumber(Integer studentNumber);
    Optional<Student> findByGradeAndClassroomAndStudentNumber(Integer grade, Integer classroom, Integer studentNumber);
    List<Student> findByNameContaining(String name);
    Optional<Student> findByEmail(String email);
    long countByGrade(Integer grade);
    long countByGradeAndClassroom(Integer grade, Integer classroom);

    Optional<Student> findByEmailAndIsEnabledTrue(String email);
    List<Student> findByIsEnabledTrue();
    List<Student> findByNameContainingAndIsEnabledTrue(String name);
    Optional<Student> findByPhoneNumber(String phoneNumber);

    // 역할별 조회 (Student는 항상 STUDENT 역할)
    @Query("SELECT s FROM Student s WHERE s.role = :role")
    List<Student> findByRole(@Param("role") UserType role);

    // 활성 사용자 조회
    @Query("SELECT s FROM Student s WHERE s.isEnabled = true")
    List<Student> findActiveUsers();

    // 역할별 활성 사용자 조회
    @Query("SELECT s FROM Student s WHERE s.role = :role AND s.isEnabled = true")
    List<Student> findActiveUsersByRole(@Param("role") UserType role);
}