package kr.hs.sdh.digitechnetwork.repository;

import kr.hs.sdh.digitechnetwork.entity.User;
import kr.hs.sdh.digitechnetwork.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User Repository
 * 사용자(User) 엔티티에 대한 데이터 접근 계층
 * 
 * 주요 기능:
 * - 사용자 기본 CRUD 작업
 * - 사용자 인증 및 권한 관리
 * - 사용자 타입별 분류 및 조회
 * - 사용자 상태 관리 (활성화/비활성화)
 * 
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 이메일로 사용자 찾기
     * 로그인 및 사용자 인증에 사용
     * 
     * @param email 사용자 이메일 (고유값)
     * @return 사용자 정보 (Optional)
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 이메일 존재 여부 확인
     * 회원가입 시 중복 이메일 검사에 사용
     * 
     * @param email 사용자 이메일
     * @return 존재 여부 (true: 존재, false: 존재하지 않음)
     */
    boolean existsByEmail(String email);
    
    /**
     * 사용자 타입별로 찾기
     * 역할 기반 사용자 분류 및 조회
     * 
     * @param role 사용자 타입 (STUDENT, TEACHER, USER, ADMIN)
     * @return 해당 타입의 사용자 목록
     */
    List<User> findByRole(UserType role);
    
    /**
     * 학생 사용자만 찾기 (JPQL 사용)
     * 학생 타입의 사용자만 조회
     * 
     * @return 학생 사용자 목록
     */
    @Query("SELECT u FROM User u WHERE u.role = 'STUDENT'")
    List<User> findStudents();
    
    /**
     * 교사 사용자만 찾기 (JPQL 사용)
     * 교사 타입의 사용자만 조회
     * 
     * @return 교사 사용자 목록
     */
    @Query("SELECT u FROM User u WHERE u.role = 'TEACHER'")
    List<User> findTeachers();
    
    /**
     * 이름으로 사용자 찾기
     * 사용자 이름 기반 부분 검색
     * 
     * @param name 사용자 이름 (부분 일치)
     * @return 이름이 포함된 사용자 목록
     */
    List<User> findByNameContaining(String name);
    
    /**
     * 전화번호로 사용자 찾기
     * 전화번호 기반 사용자 조회
     * 
     * @param phoneNumber 사용자 전화번호
     * @return 해당 전화번호를 가진 사용자 (Optional)
     */
    Optional<User> findByPhoneNumber(String phoneNumber);
    
    /**
     * 활성화된 사용자만 찾기
     * 계정이 활성화된 사용자만 조회
     * 
     * @return 활성화된 사용자 목록
     */
    @Query("SELECT u FROM User u WHERE u.isEnabled = true")
    List<User> findActiveUsers();
    
    /**
     * 특정 타입의 활성화된 사용자 찾기
     * 역할과 활성화 상태를 모두 고려한 조회
     * 
     * @param role 사용자 타입
     * @return 해당 타입의 활성화된 사용자 목록
     */
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isEnabled = true")
    List<User> findActiveUsersByRole(@Param("role") UserType role);
}
