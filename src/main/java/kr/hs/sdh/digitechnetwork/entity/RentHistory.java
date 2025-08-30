package kr.hs.sdh.digitechnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rent_histories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "rentHistory")
    private List<StudentRentHistory> studentRentHistory = new ArrayList<>();

    @OneToMany(mappedBy = "rentHistory")
    private List<TeacherRentHistory> teacherRentHistory = new ArrayList<>();
}
