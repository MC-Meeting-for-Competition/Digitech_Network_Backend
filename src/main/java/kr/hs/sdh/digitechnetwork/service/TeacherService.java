package kr.hs.sdh.digitechnetwork.service;

import kr.hs.sdh.digitechnetwork.entity.Teacher;

import java.util.List;

/**
 * Teacher Service 의 인터페이스
 * Teacher CRUD 를 담당
 *
 * @since 2025.09.01
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
public interface TeacherService {

    /**
     * 모든 교사 목록 조회
     * @return 전체 교사 목록
     */
    List<Teacher> getAllTeachers();

    /**
     * 활성화된 교사 목록 조회
     * @return 활성화된 교사 목록
     */
    List<Teacher> getActiveTeachers();

    /**
     * 전체 교사 수 조회
     * @return 전체 교사 수
     */
    long getTotalCount();

    /**
     * 활성화된 교사 수 조회
     * @return 활성화된 교사 수
     */
    long getActiveCount();

    /**
     * 교사 활성화 상태 변경
     * @param teacherId 교사 ID
     * @param isEnabled 활성화 여부
     * @return 변경된 교사 정보
     */
    Teacher updateUserStatus(Long teacherId, Boolean isEnabled);
}
