package kr.hs.sdh.digitechnetwork.repository;

import kr.hs.sdh.digitechnetwork.entity.Teacher;
import kr.hs.sdh.digitechnetwork.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    // 기존 메서드들 유지
    List<Teacher> findByNameContaining(String name);
    Optional<Teacher> findByEmail(String email);
    Optional<Teacher> findByPhoneNumber(String phoneNumber);
    List<Teacher> findByIsEnabledTrue();

    // User 관련 메서드들을 Teacher로 대체
    Optional<Teacher> findByEmailAndIsEnabledTrue(String email);
    List<Teacher> findByNameContainingAndIsEnabledTrue(String name);

    // 역할별 조회 (Teacher는 항상 TEACHER 역할)
    @Query("SELECT t FROM Teacher t WHERE t.role = :role")
    List<Teacher> findByRole(@Param("role") UserType role);

    // 활성 사용자 조회
    @Query("SELECT t FROM Teacher t WHERE t.isEnabled = true")
    List<Teacher> findActiveUsers();

    // 역할별 활성 사용자 조회
    @Query("SELECT t FROM Teacher t WHERE t.role = :role AND t.isEnabled = true")
    List<Teacher> findActiveUsersByRole(@Param("role") UserType role);
}