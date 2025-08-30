package kr.hs.sdh.digitechnetwork.repository;

import kr.hs.sdh.digitechnetwork.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // 학년별로 학생 찾기
    List<Student> findByGrade(Integer grade);
    
    // 반별로 학생 찾기
    List<Student> findByClassroom(Integer classroom);
    
    // 학년과 반으로 학생 찾기
    List<Student> findByGradeAndClassroom(Integer grade, Integer classroom);
    
    // 학번으로 학생 찾기
    Optional<Student> findByStudentNumber(Integer studentNumber);
    
    // 학년, 반, 번호로 학생 찾기
    Optional<Student> findByGradeAndClassroomAndStudentNumber(
        Integer grade, Integer classroom, Integer studentNumber);
    
    // User와의 관계를 통해 이름으로 학생 찾기
    @Query("SELECT s FROM Student s WHERE s.user.name LIKE %:name%")
    List<Student> findByUserNameContaining(@Param("name") String name);
    
    // User와의 관계를 통해 이메일로 학생 찾기
    @Query("SELECT s FROM Student s WHERE s.user.email = :email")
    Optional<Student> findByUserEmail(@Param("email") String email);
    
    // 특정 학년의 학생 수 조회
    @Query("SELECT COUNT(s) FROM Student s WHERE s.grade = :grade")
    long countByGrade(@Param("grade") Integer grade);
    
    // 특정 반의 학생 수 조회
    @Query("SELECT COUNT(s) FROM Student s WHERE s.grade = :grade AND s.classroom = :classroom")
    long countByGradeAndClassroom(@Param("grade") Integer grade, @Param("classroom") Integer classroom);
}
