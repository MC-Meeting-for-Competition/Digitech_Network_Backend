package kr.hs.sdh.digitechnetwork.enums;

import lombok.Getter;

@Getter
public enum UserType {
    USER("사용자"),
    ADMIN("관리자");

    private final String description;

    UserType(String description) {
        this.description = description;
    }
}
