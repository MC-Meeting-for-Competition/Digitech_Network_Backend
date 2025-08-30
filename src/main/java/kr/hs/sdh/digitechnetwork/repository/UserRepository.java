package kr.hs.sdh.digitechnetwork.repository;

import kr.hs.sdh.digitechnetwork.entity.User;
import kr.hs.sdh.digitechnetwork.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // 이메일로 사용자 찾기
    Optional<User> findByEmail(String email);
    
    // 이메일 존재 여부 확인
    boolean existsByEmail(String email);
    
    // 사용자 타입별로 찾기
    List<User> findByRole(UserType role);
    
    // 이름으로 사용자 찾기
    List<User> findByNameContaining(String name);
    
    // 전화번호로 사용자 찾기
    Optional<User> findByPhoneNumber(String phoneNumber);
    
    // 활성화된 사용자만 찾기
    @Query("SELECT u FROM User u WHERE u.isEnabled = true")
    List<User> findActiveUsers();
}
