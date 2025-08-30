package kr.hs.sdh.digitechnetwork.entity;

import jakarta.persistence.*;
import kr.hs.sdh.digitechnetwork.enums.UserType;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 */
@Entity
@Table(name = "students")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Student extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

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

    public Student changeRole(UserType role) {
        this.role = role;
        return this;
    }

    public Student changeGrade(Integer grade) {
        this.grade = grade;
        return this;
    }

    public Student changeClassroom(Integer classroom) {
        this.classroom = classroom;
        return this;
    }

    public Student changeStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
        return this;
    }

    public Student changeBio(String bio) {
        this.bio = bio;
        return this;
    }

//   <----------- UserDetails Methods ----------->

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
        return email; // 인증 ID로 email 사용
    }
}
