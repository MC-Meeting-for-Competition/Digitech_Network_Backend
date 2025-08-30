package kr.hs.sdh.digitechnetwork.repository;

import kr.hs.sdh.digitechnetwork.entity.RentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentHistoryRepository extends JpaRepository<RentHistory, Long> {
    // 기본 CRUD 메서드는 JpaRepository에서 자동 제공
    // 복잡한 조회는 StudentRentHistoryRepository와 TeacherRentHistoryRepository에서 처리
}
