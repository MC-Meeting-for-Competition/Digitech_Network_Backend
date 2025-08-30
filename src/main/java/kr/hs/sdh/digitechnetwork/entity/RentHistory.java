package kr.hs.sdh.digitechnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rent_histories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RentHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rent_histories_id")
    private Long id;
}
