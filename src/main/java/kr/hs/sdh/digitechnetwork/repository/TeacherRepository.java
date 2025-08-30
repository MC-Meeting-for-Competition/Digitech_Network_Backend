package kr.hs.sdh.digitechnetwork.repository;

import kr.hs.sdh.digitechnetwork.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Teacher Repository
 * 교사(Teacher) 엔티티에 대한 데이터 접근 계층
 * 
 * 주요 기능:
 * - 교사 기본 CRUD 작업
 * - 교사와 User 엔티티 간의 관계를 통한 정보 접근
 * - 교사별 대여 이력 통계 조회
 * - 활성화된 교사 조회
 * 
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    /**
     * User와의 관계를 통해 이메일로 교사 찾기
     * User 엔티티의 이메일을 통해 교사 검색
     * 
     * @param email 사용자 이메일
     * @return 해당 이메일을 가진 교사 (Optional)
     */
    @Query("SELECT t FROM Teacher t WHERE t.user.email = :email")
    Optional<Teacher> findByUserEmail(@Param("email") String email);
    
    /**
     * User와의 관계를 통해 이름으로 교사 찾기
     * User 엔티티의 이름을 통해 교사 검색
     * 
     * @param name 사용자 이름 (부분 일치)
     * @return 이름이 포함된 교사 목록
     */
    @Query("SELECT t FROM Teacher t WHERE t.user.name LIKE %:name%")
    List<Teacher> findByUserNameContaining(@Param("name") String name);
    
    /**
     * User와의 관계를 통해 전화번호로 교사 찾기
     * User 엔티티의 전화번호를 통해 교사 검색
     * 
     * @param phoneNumber 사용자 전화번호
     * @return 해당 전화번호를 가진 교사 (Optional)
     */
    @Query("SELECT t FROM Teacher t WHERE t.user.phoneNumber = :phoneNumber")
    Optional<Teacher> findByUserPhoneNumber(@Param("phoneNumber") String phoneNumber);
    
    /**
     * 활성화된 교사만 찾기
     * 계정이 활성화된 교사만 조회
     * 
     * @return 활성화된 교사 목록
     */
    @Query("SELECT t FROM Teacher t WHERE t.user.isEnabled = true")
    List<Teacher> findActiveTeachers();
    
    /**
     * 특정 교사의 대여 이력 수 조회
     * 교사별 대여 통계 정보 생성에 사용
     * 
     * @param teacherId 교사 ID
     * @return 해당 교사의 대여 이력 수
     */
    @Query("SELECT COUNT(trh) FROM TeacherRentHistory trh WHERE trh.teacher.id = :teacherId")
    long countRentHistoryByTeacherId(@Param("teacherId") Long teacherId);
}
