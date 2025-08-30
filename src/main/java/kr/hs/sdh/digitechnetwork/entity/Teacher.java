package kr.hs.sdh.digitechnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Teacher extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String bio;

    @OneToMany(mappedBy = "teacher")
    private List<TeacherRentHistory> teacherRentHistoryArrayList = new ArrayList<>();

    public Teacher(String name, String hashedPassword, String email, String phoneNumber) {
        // User 생성 로직은 Service 계층에서 처리
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

    public String getRole() {
        return user != null ? user.getRole().toString() : null;
    }
}
