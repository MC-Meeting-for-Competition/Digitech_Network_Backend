package kr.hs.sdh.digitechnetwork.controller;

import kr.hs.sdh.digitechnetwork.dto.EquipmentCreateRequestDto;
import kr.hs.sdh.digitechnetwork.enums.EquipmentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * EquipmentController 보안 테스트
 * 관리자 권한이 필요한 엔드포인트들의 접근 제어를 테스트
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class EquipmentControllerSecurityTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("기자재 등록 - 관리자 권한 없이 접근 시 403 Forbidden")
    @WithMockUser(roles = "STUDENT")
    void registerEquipment_WithoutAdminRole_ShouldReturnForbidden() throws Exception {
        EquipmentCreateRequestDto requestDto = EquipmentCreateRequestDto.builder()
                .identifier("TEST-EQ-001")
                .name("테스트 기자재")
                .equipmentTypeId(1L)
                .build();

        mockMvc.perform(post("/api/v1/equipment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"identifier\":\"TEST-EQ-001\",\"name\":\"테스트 기자재\",\"equipmentTypeId\":1}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("기자재 등록 - 관리자 권한으로 접근 시 201 Created")
    @WithMockUser(roles = "ADMIN")
    void registerEquipment_WithAdminRole_ShouldReturnCreated() throws Exception {
        mockMvc.perform(post("/api/v1/equipment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"identifier\":\"TEST-EQ-001\",\"name\":\"테스트 기자재\",\"equipmentTypeId\":1}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("기자재 수정 - 관리자 권한 없이 접근 시 403 Forbidden")
    @WithMockUser(roles = "STUDENT")
    void updateEquipment_WithoutAdminRole_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(put("/api/v1/equipment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"identifier\":\"TEST-EQ-001\",\"name\":\"테스트 기자재\",\"equipmentTypeId\":1,\"status\":\"AVAILABLE\",\"isPublic\":true}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("기자재 삭제 - 관리자 권한 없이 접근 시 403 Forbidden")
    @WithMockUser(roles = "STUDENT")
    void deleteEquipment_WithoutAdminRole_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(delete("/api/v1/equipment/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("기자재 상태 변경 - 관리자 권한 없이 접근 시 403 Forbidden")
    @WithMockUser(roles = "STUDENT")
    void changeEquipmentStatus_WithoutAdminRole_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(patch("/api/v1/equipment/1/status")
                        .param("status", EquipmentStatus.AVAILABLE.name()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("기자재 공개 설정 변경 - 관리자 권한 없이 접근 시 403 Forbidden")
    @WithMockUser(roles = "STUDENT")
    void setEquipmentPublicity_WithoutAdminRole_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(patch("/api/v1/equipment/1/publicity")
                        .param("isPublic", "true"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("기자재 목록 조회 - 모든 사용자가 접근 가능")
    @WithMockUser(roles = "STUDENT")
    void getEquipmentList_ShouldBeAccessible() throws Exception {
        mockMvc.perform(get("/api/v1/equipment"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("기자재 상세 조회 - 모든 사용자가 접근 가능")
    @WithMockUser(roles = "STUDENT")
    void getEquipmentById_ShouldBeAccessible() throws Exception {
        mockMvc.perform(get("/api/v1/equipment/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("공개 기자재 목록 조회 - 모든 사용자가 접근 가능")
    @WithMockUser(roles = "STUDENT")
    void getPublicEquipments_ShouldBeAccessible() throws Exception {
        mockMvc.perform(get("/api/v1/equipment/public"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용 가능한 기자재 목록 조회 - 모든 사용자가 접근 가능")
    @WithMockUser(roles = "STUDENT")
    void getAvailableEquipments_ShouldBeAccessible() throws Exception {
        mockMvc.perform(get("/api/v1/equipment/available"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("기자재 통계 조회 - 모든 사용자가 접근 가능")
    @WithMockUser(roles = "STUDENT")
    void getEquipmentStatistics_ShouldBeAccessible() throws Exception {
        mockMvc.perform(get("/api/v1/equipment/statistics"))
                .andExpect(status().isOk());
    }
}
