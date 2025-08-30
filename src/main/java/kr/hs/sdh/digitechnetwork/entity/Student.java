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
@Table(name = "students")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Student extends BaseEntity implements UserDetails {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String hashedPassword;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserType role = UserType.STUDENT;

    @Column(nullable = false)
    private Boolean isEnabled = true;

    @Column
    private String bio;

    @Column
    private Integer grade; // 학년

    @Column
    private Integer classroom; // 반

    @Column
    private Integer studentNumber; // 번호

    @OneToMany(mappedBy = "student")
    private List<StudentRentHistory> studentRentHistoryArrayList = new ArrayList<>();

    // 유효성 검증이 포함된 setter 메서드들
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
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

    public void setHashedPassword(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 비어있을 수 없습니다.");
        }
        this.hashedPassword = hashedPassword;
    }

    public void setRole(UserType role) {
        if (role == null || !UserType.STUDENT.equals(role)) {
            throw new IllegalArgumentException("Student의 역할은 STUDENT여야 합니다.");
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

    public void setGrade(Integer grade) {
        if (grade != null && (grade < 1 || grade > 6)) {
            throw new IllegalArgumentException("학년은 1~6 사이여야 합니다.");
        }
        this.grade = grade;
    }

    public void setClassroom(Integer classroom) {
        if (classroom != null && (classroom < 1 || classroom > 20)) {
            throw new IllegalArgumentException("반은 1~20 사이여야 합니다.");
        }
        this.classroom = classroom;
    }

    public void setStudentNumber(Integer studentNumber) {
        if (studentNumber != null && (studentNumber < 1 || studentNumber > 50)) {
            throw new IllegalArgumentException("번호는 1~50 사이여야 합니다.");
        }
        this.studentNumber = studentNumber;
    }

    // 변경 메서드들 (체이닝 지원)
    public Student changeGrade(Integer grade) {
        setGrade(grade);
        return this;
    }

    public Student changeClassroom(Integer classroom) {
        setClassroom(classroom);
        return this;
    }

    public Student changeStudentNumber(Integer studentNumber) {
        setStudentNumber(studentNumber);
        return this;
    }

    // 편의 메서드들
    public Student changePhoneNumber(String newPhoneNumber) {
        setPhoneNumber(newPhoneNumber);
        return this;
    }

    public Student changeBio(String bio) {
        setBio(bio);
        return this;
    }

    public Student changeIsEnabled(boolean isEnabled) {
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
        return hashedPassword;
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
    public boolean isInSameClass(Student other) {
        if (other == null) return false;
        return this.grade.equals(other.grade) &&
                this.classroom.equals(other.classroom);
    }

    public String getFullClassInfo() {
        return String.format("%d학년 %d반 %d번", grade, classroom, studentNumber);
    }

    public String getDisplayName() {
        return this.name + " (" + this.role + ")";
    }
}