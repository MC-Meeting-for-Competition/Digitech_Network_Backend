package kr.hs.sdh.digitechnetwork.entity;

import jakarta.persistence.*;
import kr.hs.sdh.digitechnetwork.enums.UserType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Student extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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

    // User 정보에 대한 편의 메서드
    public String getName() {
        return user != null ? user.getName() : null;
    }

    public String getEmail() {
        return user != null ? user.getEmail() : null;
    }

    public String getPhoneNumber() {
        return user != null ? user.getPhoneNumber() : null;
    }

    public UserType getRole() {
        return user != null ? user.getRole() : null;
    }
}
