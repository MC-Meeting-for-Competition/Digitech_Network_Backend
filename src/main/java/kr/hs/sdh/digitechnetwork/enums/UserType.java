package kr.hs.sdh.digitechnetwork.enums;

import lombok.Getter;

@Getter
public enum UserType {
    STUDENT("학생"),
    TEACHER("교사"),
    ADMIN("관리자");

    private final String description;

    UserType(String description) {
        this.description = description;
    }
}
