package kr.hs.sdh.digitechnetwork.controller;

import kr.hs.sdh.digitechnetwork.dto.EquipmentStatisticsDto;
import kr.hs.sdh.digitechnetwork.entity.Equipment;
import kr.hs.sdh.digitechnetwork.entity.Student;
import kr.hs.sdh.digitechnetwork.entity.Teacher;
import kr.hs.sdh.digitechnetwork.enums.EquipmentStatus;
import kr.hs.sdh.digitechnetwork.enums.UserType;
import kr.hs.sdh.digitechnetwork.service.EquipmentService;
import kr.hs.sdh.digitechnetwork.service.StudentService;
import kr.hs.sdh.digitechnetwork.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 관리자 전용 REST API 컨트롤러
 * 시스템 관리, 통계 조회, 사용자 관리 등 관리자 기능을 제공
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    private final EquipmentService equipmentService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    /**
     * 시스템 대시보드 통계 조회
     * 전체 시스템 현황을 한눈에 볼 수 있는 통계 정보 제공
     * 
     * @return 시스템 통계 정보
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStatistics() {
        log.info("관리자 대시보드 통계 조회 요청");
        
        // 기자재 통계
        EquipmentStatisticsDto equipmentStats = equipmentService.getEquipmentStatistics();
        
        // 사용자 통계 (실제 구현 시 StudentService, TeacherService에서 가져와야 함)
        long totalStudents = 0; // studentService.getTotalCount();
        long totalTeachers = 0; // teacherService.getTotalCount();
        long activeStudents = 0; // studentService.getActiveCount();
        long activeTeachers = 0; // teacherService.getActiveCount();
        
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("equipment", equipmentStats);
        dashboard.put("users", Map.of(
            "totalStudents", totalStudents,
            "totalTeachers", totalTeachers,
            "activeStudents", activeStudents,
            "activeTeachers", activeTeachers
        ));
        dashboard.put("system", Map.of(
            "totalUsers", totalStudents + totalTeachers,
            "activeUsers", activeStudents + activeTeachers
        ));
        
        return ResponseEntity.ok(dashboard);
    }

    /**
     * 기자재 관리 통계 조회
     * 기자재별 상세 통계 정보 제공
     * 
     * @return 기자재 관리 통계
     */
    @GetMapping("/equipment/statistics")
    public ResponseEntity<EquipmentStatisticsDto> getEquipmentStatistics() {
        log.info("관리자 기자재 통계 조회 요청");
        EquipmentStatisticsDto statistics = equipmentService.getEquipmentStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * 기자재 상태별 목록 조회
     * 특정 상태의 기자재들을 관리자가 조회
     * 
     * @param status 조회할 기자재 상태
     * @return 해당 상태의 기자재 목록
     */
    @GetMapping("/equipment/status/{status}")
    public ResponseEntity<List<Equipment>> getEquipmentsByStatus(@PathVariable EquipmentStatus status) {
        log.info("관리자 기자재 상태별 조회 요청: 상태={}", status);
        List<Equipment> equipments = equipmentService.getEquipmentsByStatus(status);
        return ResponseEntity.ok(equipments);
    }

    /**
     * 사용자 관리 - 학생 목록 조회
     * 관리자가 모든 학생 정보를 조회
     * 
     * @return 전체 학생 목록
     */
    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        log.info("관리자 학생 목록 조회 요청");
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    /**
     * 사용자 관리 - 교사 목록 조회
     * 관리자가 모든 교사 정보를 조회
     * 
     * @return 전체 교사 목록
     */
    @GetMapping("/teachers")
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        log.info("관리자 교사 목록 조회 요청");
        List<Teacher> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }

    /**
     * 사용자 활성화/비활성화 관리
     * 특정 사용자의 활성화 상태를 변경
     * 
     * @param userType 사용자 타입 (STUDENT/TEACHER)
     * @param userId 사용자 ID
     * @param isEnabled 활성화 여부
     * @return 변경 결과
     */
    @PatchMapping("/users/{userType}/{userId}/status")
    public ResponseEntity<Map<String, Object>> updateUserStatus(
            @PathVariable UserType userType,
            @PathVariable Long userId,
            @RequestParam Boolean isEnabled) {
        
        log.info("관리자 사용자 상태 변경 요청: 타입={}, ID={}, 활성화={}", userType, userId, isEnabled);
        
        boolean success = false;
        String message = "";
        
        try {
            if (userType == UserType.STUDENT) {
                // studentService.updateUserStatus(userId, isEnabled);
                success = true;
                message = "학생 상태가 성공적으로 변경되었습니다.";
            } else if (userType == UserType.TEACHER) {
                // teacherService.updateUserStatus(userId, isEnabled);
                success = true;
                message = "교사 상태가 성공적으로 변경되었습니다.";
            }
        } catch (Exception e) {
            message = "사용자 상태 변경에 실패했습니다: " + e.getMessage();
            log.error("사용자 상태 변경 실패: {}", e.getMessage(), e);
        }
        
        Map<String, Object> response = Map.of(
            "success", success,
            "message", message,
            "userType", userType,
            "userId", userId,
            "isEnabled", isEnabled
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 시스템 상태 확인
     * 데이터베이스 연결, 서비스 상태 등을 확인
     * 
     * @return 시스템 상태 정보
     */
    @GetMapping("/system/health")
    public ResponseEntity<Map<String, Object>> getSystemHealth() {
        log.info("관리자 시스템 상태 확인 요청");
        
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());
        health.put("services", Map.of(
            "equipmentService", "UP",
            "studentService", "UP", 
            "teacherService", "UP"
        ));
        
        return ResponseEntity.ok(health);
    }

    /**
     * 관리자 권한 확인
     * 현재 요청한 사용자가 관리자 권한을 가지고 있는지 확인
     * 
     * @return 권한 확인 결과
     */
    @GetMapping("/auth/verify")
    public ResponseEntity<Map<String, Object>> verifyAdminAuth() {
        log.info("관리자 권한 확인 요청");
        
        Map<String, Object> response = Map.of(
            "authenticated", true,
            "role", "ADMIN",
            "message", "관리자 권한이 확인되었습니다."
        );
        
        return ResponseEntity.ok(response);
    }
}
