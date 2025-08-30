package kr.hs.sdh.digitechnetwork.repository;

import kr.hs.sdh.digitechnetwork.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Student Repository
 * 학생(Student) 엔티티에 대한 데이터 접근 계층
 * 
 * 주요 기능:
 * - 학생 기본 CRUD 작업
 * - 학년/반/번호별 학생 분류 및 조회
 * - 학생과 User 엔티티 간의 관계를 통한 정보 접근
 * - 학생별 통계 정보 조회
 * 
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    /**
     * 학년별로 학생 찾기
     * 특정 학년의 모든 학생 조회
     * 
     * @param grade 학년 (1, 2, 3)
     * @return 해당 학년의 학생 목록
     */
    List<Student> findByGrade(Integer grade);
    
    /**
     * 반별로 학생 찾기
     * 특정 반의 모든 학생 조회
     * 
     * @param classroom 반 번호
     * @return 해당 반의 학생 목록
     */
    List<Student> findByClassroom(Integer classroom);
    
    /**
     * 학년과 반으로 학생 찾기
     * 특정 학년의 특정 반 학생 조회
     * 
     * @param grade 학년
     * @param classroom 반 번호
     * @return 해당 학년/반의 학생 목록
     */
    List<Student> findByGradeAndClassroom(Integer grade, Integer classroom);
    
    /**
     * 학번으로 학생 찾기
     * 학생의 고유 번호로 조회
     * 
     * @param studentNumber 학생 번호
     * @return 해당 번호의 학생 (Optional)
     */
    Optional<Student> findByStudentNumber(Integer studentNumber);
    
    /**
     * 학년, 반, 번호로 학생 찾기
     * 학생을 고유하게 식별하는 조회
     * 
     * @param grade 학년
     * @param classroom 반 번호
     * @param studentNumber 학생 번호
     * @return 해당 조건의 학생 (Optional)
     */
    Optional<Student> findByGradeAndClassroomAndStudentNumber(
        Integer grade, Integer classroom, Integer studentNumber);
    
    /**
     * User와의 관계를 통해 이름으로 학생 찾기
     * User 엔티티의 이름을 통해 학생 검색
     * 
     * @param name 사용자 이름 (부분 일치)
     * @return 이름이 포함된 학생 목록
     */
    @Query("SELECT s FROM Student s WHERE s.user.name LIKE %:name%")
    List<Student> findByUserNameContaining(@Param("name") String name);
    
    /**
     * User와의 관계를 통해 이메일로 학생 찾기
     * User 엔티티의 이메일을 통해 학생 검색
     * 
     * @param email 사용자 이메일
     * @return 해당 이메일을 가진 학생 (Optional)
     */
    @Query("SELECT s FROM Student s WHERE s.user.email = :email")
    Optional<Student> findByUserEmail(@Param("email") String email);
    
    /**
     * 특정 학년의 학생 수 조회
     * 학년별 학생 통계 정보 생성에 사용
     * 
     * @param grade 학년
     * @return 해당 학년의 학생 수
     */
    @Query("SELECT COUNT(s) FROM Student s WHERE s.grade = :grade")
    long countByGrade(@Param("grade") Integer grade);
    
    /**
     * 특정 반의 학생 수 조회
     * 반별 학생 통계 정보 생성에 사용
     * 
     * @param grade 학년
     * @param classroom 반 번호
     * @return 해당 반의 학생 수
     */
    @Query("SELECT COUNT(s) FROM Student s WHERE s.grade = :grade AND s.classroom = :classroom")
    long countByGradeAndClassroom(@Param("grade") Integer grade, @Param("classroom") Integer classroom);
}
