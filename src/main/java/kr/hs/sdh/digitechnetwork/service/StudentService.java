package kr.hs.sdh.digitechnetwork.service;

import kr.hs.sdh.digitechnetwork.entity.Student;

import java.util.List;

/**
 * Student Service 의 인터페이스
 * Student의 CRUD 를 담당
 *
 * @since 2025.09.01
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
public interface StudentService {

    /**
     * 모든 학생 목록 조회
     * @return 전체 학생 목록
     */
    List<Student> getAllStudents();

    /**
     * 활성화된 학생 목록 조회
     * @return 활성화된 학생 목록
     */
    List<Student> getActiveStudents();

    /**
     * 전체 학생 수 조회
     * @return 전체 학생 수
     */
    long getTotalCount();

    /**
     * 활성화된 학생 수 조회
     * @return 활성화된 학생 수
     */
    long getActiveCount();

    /**
     * 학생 활성화 상태 변경
     * @param studentId 학생 ID
     * @param isEnabled 활성화 여부
     * @return 변경된 학생 정보
     */
    Student updateUserStatus(Long studentId, Boolean isEnabled);
}
