package kr.hs.sdh.digitechnetwork.entity;

import jakarta.persistence.*;
import kr.hs.sdh.digitechnetwork.enums.UserType;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Teacher extends BaseEntity implements UserDetails {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserType role = UserType.TEACHER;

    @Column(nullable = false)
    private Boolean isEnabled = true;

    @Column
    private String bio;

    @OneToMany(mappedBy = "teacher")
    private List<TeacherRentHistory> teacherRentHistoryArrayList = new ArrayList<>();

    // 유효성 검증이 포함된 setter 메서드들
    public void setName(String name) {
        if (name == null || name.trim().isBlank()) {
            throw new IllegalArgumentException("이름은 비어있을 수 없습니다.");
        }
        this.name = name.trim();
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@") || email.trim().isEmpty()) {
            throw new IllegalArgumentException("유효한 이메일 형식이 아닙니다.");
        }
        this.email = email.trim().toLowerCase();
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("^\\d{3}-\\d{3,4}-\\d{4}$")) {
            throw new IllegalArgumentException("전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)");
        }
        this.phoneNumber = phoneNumber;
    }
    public void setRole(UserType role) {
        if (!UserType.TEACHER.equals(role)) {
            throw new IllegalArgumentException("Teacher의 역할은 TEACHER여야 합니다.");
        }
        this.role = role;
    }

    public void setIsEnabled(Boolean isEnabled) {
        if (isEnabled == null) {
            throw new IllegalArgumentException("활성화 상태는 null일 수 없습니다.");
        }
        this.isEnabled = isEnabled;
    }

    public void setBio(String bio) {
        this.bio = bio != null ? bio.trim() : null;
    }

    // 편의 메서드들
    public Teacher changePhoneNumber(String newPhoneNumber) {
        setPhoneNumber(newPhoneNumber);
        return this;
    }

    public Teacher changeBio(String bio) {
        setBio(bio);
        return this;
    }

    public Teacher changeIsEnabled(boolean isEnabled) {
        setIsEnabled(isEnabled);
        return this;
    }

    // UserDetails 인터페이스 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    // 비즈니스 로직 메서드
    public boolean isActive() {
        return this.isEnabled;
    }

    public String getDisplayName() {
        if (this.name != null) {
            return this.name + " 선생님";
        }
        return "알 수 없는 선생님";
    }
}