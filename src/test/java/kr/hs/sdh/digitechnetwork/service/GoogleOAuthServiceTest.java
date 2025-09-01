package kr.hs.sdh.digitechnetwork.service;

import kr.hs.sdh.digitechnetwork.auth.GoogleOAuth;
import kr.hs.sdh.digitechnetwork.dto.AuthResponseDto;
import kr.hs.sdh.digitechnetwork.dto.GoogleOAuthRequestDto;
import kr.hs.sdh.digitechnetwork.entity.Student;
import kr.hs.sdh.digitechnetwork.enums.UserType;
import kr.hs.sdh.digitechnetwork.repository.StudentRepository;
import kr.hs.sdh.digitechnetwork.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleOAuthServiceTest {

    @Mock
    private GoogleOAuth googleOAuth;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private RestTemplate restTemplate;

    private GoogleOAuthServiceImpl googleOAuthService;

    private GoogleOAuthRequestDto requestDto;
    private Map<String, Object> tokenResponseMap;
    private Map<String, Object> userInfoMap;

    @BeforeEach
    void setUp() {
        googleOAuthService = new GoogleOAuthServiceImpl(
                googleOAuth, 
                studentRepository, 
                teacherRepository, 
                restTemplate, 
                new com.fasterxml.jackson.databind.ObjectMapper()
        );
        
        requestDto = GoogleOAuthRequestDto.builder()
                .code("test_auth_code")
                .state("test_state")
                .build();

        tokenResponseMap = new HashMap<>();
        tokenResponseMap.put("access_token", "test_access_token");
        tokenResponseMap.put("refresh_token", "test_refresh_token");
        tokenResponseMap.put("token_type", "Bearer");
        tokenResponseMap.put("expires_in", 3600L);
        tokenResponseMap.put("scope", "profile email");
        tokenResponseMap.put("id_token", "test_id_token");

        userInfoMap = new HashMap<>();
        userInfoMap.put("id", "test_google_id");
        userInfoMap.put("email", "test@example.com");
        userInfoMap.put("name", "Test User");
        userInfoMap.put("given_name", "Test");
        userInfoMap.put("family_name", "User");
        userInfoMap.put("picture", "https://example.com/picture.jpg");
        userInfoMap.put("locale", "ko");
        userInfoMap.put("verified_email", true);
    }

    @Test
    @DisplayName("Google OAuth 인증 URL 생성 테스트")
    void getAuthorizationUrl() {
        // given
        String expectedUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=test&redirect_uri=test&scope=test&response_type=code&access_type=offline&prompt=consent&state=test";
        when(googleOAuth.getRedirectUri()).thenReturn(expectedUrl);

        // when
        String result = googleOAuthService.getAuthorizationUrl();

        // then
        assertThat(result).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("기존 학생 사용자로 Google OAuth 인증 성공 테스트")
    void authenticateWithExistingStudent() {
        // given
        Student existingStudent = Student.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .role(UserType.STUDENT)
                .isEnabled(true)
                .grade(1)
                .classroom(1)
                .studentNumber(1)
                .build();

        when(studentRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existingStudent));
        when(teacherRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(tokenResponseMap, HttpStatus.OK));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(userInfoMap, HttpStatus.OK));

        // when
        AuthResponseDto result = googleOAuthService.authenticate(requestDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isEqualTo("test_access_token");
        assertThat(result.getUserInfo()).isNotNull();
        assertThat(result.getUserInfo().getEmail()).isEqualTo("test@example.com");
        assertThat(result.getUserInfo().getName()).isEqualTo("Test User");
        assertThat(result.getUserInfo().getRole()).isEqualTo(UserType.STUDENT);
    }

    @Test
    @DisplayName("새로운 사용자로 Google OAuth 인증 성공 테스트")
    void authenticateWithNewUser() {
        // given
        Student newStudent = Student.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .role(UserType.STUDENT)
                .isEnabled(true)
                .grade(1)
                .classroom(1)
                .studentNumber(1)
                .build();

        when(studentRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(teacherRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(tokenResponseMap, HttpStatus.OK));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(userInfoMap, HttpStatus.OK));

        // when
        AuthResponseDto result = googleOAuthService.authenticate(requestDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isEqualTo("test_access_token");
        assertThat(result.getUserInfo()).isNotNull();
        assertThat(result.getUserInfo().getEmail()).isEqualTo("test@example.com");
        assertThat(result.getUserInfo().getName()).isEqualTo("Test User");
        assertThat(result.getUserInfo().getRole()).isEqualTo(UserType.STUDENT);
    }

    @Test
    @DisplayName("토큰 요청 실패 시 예외 발생 테스트")
    void authenticateWithTokenRequestFailure() {
        // given
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        // when & then
        assertThatThrownBy(() -> googleOAuthService.authenticate(requestDto))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("사용자 정보 요청 실패 시 예외 발생 테스트")
    void authenticateWithUserInfoRequestFailure() {
        // given
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(tokenResponseMap, HttpStatus.OK));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        // when & then
        assertThatThrownBy(() -> googleOAuthService.authenticate(requestDto))
                .isInstanceOf(RuntimeException.class);
    }
}
