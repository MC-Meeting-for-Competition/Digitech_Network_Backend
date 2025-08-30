package kr.hs.sdh.digitechnetwork.repository;

import kr.hs.sdh.digitechnetwork.entity.RentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * RentHistory Repository
 * 대여 이력(RentHistory) 엔티티에 대한 데이터 접근 계층
 * 
 * 주요 기능:
 * - 대여 이력 기본 CRUD 작업
 * - 대여 이력의 단순한 관리
 * - 복잡한 조회는 StudentRentHistoryRepository와 TeacherRentHistoryRepository에서 처리
 * 
 * 현재 구조에서는 RentHistory가 단순한 ID 엔티티로 사용되며,
 * 실제 대여 관련 정보는 중간 테이블(StudentRentHistory, TeacherRentHistory)에서 관리됩니다.
 * 
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Repository
public interface RentHistoryRepository extends JpaRepository<RentHistory, Long> {
    // 기본 CRUD 메서드는 JpaRepository에서 자동 제공
    // 복잡한 조회는 StudentRentHistoryRepository와 TeacherRentHistoryRepository에서 처리
}
