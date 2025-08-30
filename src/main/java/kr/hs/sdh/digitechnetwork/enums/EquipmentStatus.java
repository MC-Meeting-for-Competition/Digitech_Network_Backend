package kr.hs.sdh.digitechnetwork.enums;

import lombok.*;

/**
 * 기자재의 상태를 ENUM 만들어 데이터베이스에 저장
 *
 * @since 2025.08.29
 * @author yunjisang - sdh230308@sdh.hs.kr
 * @version 0.0.1
 */
@Getter
public enum EquipmentStatus {
    AVAILABLE("대여 가능한 기자재"),
    RENT("대여 중인 기자재"),
    BROKEN("고장난 기자재"),
    CHECK("반납 처리 중인 기자재"),
    UNAVAILABLE("사용이 불가능한 기자재"),
    FIX("수리 중인 기자재");

    private final String message;

    EquipmentStatus(String message) {
        this.message = message;
    }
}