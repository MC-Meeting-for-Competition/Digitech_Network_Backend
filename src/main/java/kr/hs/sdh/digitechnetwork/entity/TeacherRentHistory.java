package kr.hs.sdh.digitechnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teacher_rent_histories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TeacherRentHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rent_histories_id")
    private RentHistory rentHistory;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}
