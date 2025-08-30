package kr.hs.sdh.digitechnetwork.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "student_rent_histories")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StudentRentHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_rent_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rent_history_id")
    private RentHistory rentHistory;

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setRentHistory(RentHistory rentHistory) {
        this.rentHistory = rentHistory;
    }
}