package kr.hs.sdh.digitechnetwork.entity;

import kr.hs.sdh.digitechnetwork.enums.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teacher 엔티티 테스트 클래스
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 */
@DisplayName("Teacher 엔티티 테스트")
class TeacherTest {

    private Teacher teacher;
    private Teacher teacher2;

    @BeforeEach
    void setUp() {
        teacher = Teacher.builder()
                .id(1L)
                .name("김교사")
                .hashedPassword("password123")
                .email("teacher@test.com")
                .phoneNumber("010-1234-5678")
                .role(UserType.TEACHER)
                .isEnabled(true)
                .bio("수학을 가르치는 교사입니다.")
                .build();

        teacher2 = Teacher.builder()
                .id(2L)
                .name("이교사")
                .hashedPassword("password456")
                .email("teacher2@test.com")
                .phoneNumber("010-2345-6789")
                .role(UserType.TEACHER)
                .isEnabled(true)
                .bio("영어를 가르치는 교사입니다.")
                .build();
    }

    @Test
    @DisplayName("Teacher 빌더 패턴 테스트")
    void testBuilderPattern() {
        // Given & When
        Teacher newTeacher = Teacher.builder()
                .id(3L)
                .name("박교사")
                .hashedPassword("password789")
                .email("teacher3@test.com")
                .phoneNumber("010-3456-7890")
                .role(UserType.TEACHER)
                .isEnabled(true)
                .bio("과학을 가르치는 교사입니다.")
                .build();

        // Then
        assertNotNull(newTeacher);
        assertEquals(3L, newTeacher.getId());
        assertEquals("박교사", newTeacher.getName());
        assertEquals("password789", newTeacher.getHashedPassword());
        assertEquals("teacher3@test.com", newTeacher.getEmail());
        assertEquals("010-3456-7890", newTeacher.getPhoneNumber());
        assertEquals(UserType.TEACHER, newTeacher.getRole());
        assertTrue(newTeacher.getIsEnabled());
        assertEquals("과학을 가르치는 교사입니다.", newTeacher.getBio());
    }

    @Test
    @DisplayName("Teacher 기본 생성자 테스트")
    void testDefaultConstructor() {
        // Given & When
        Teacher newTeacher = new Teacher();

        // Then
        assertNotNull(newTeacher);
        assertNull(newTeacher.getId());
        assertNull(newTeacher.getName());
        assertNull(newTeacher.getHashedPassword());
        assertNull(newTeacher.getEmail());
        assertNull(newTeacher.getPhoneNumber());
        assertSame(UserType.TEACHER, newTeacher.getRole());
        assertTrue(newTeacher.getIsEnabled());
        assertNull(newTeacher.getBio());
    }

    @Test
    @DisplayName("Teacher Setter 메서드 테스트")
    void testSetterMethods() {
        // Given
        Teacher newTeacher = new Teacher();

        // When
        newTeacher.setId(4L);
        newTeacher.setName("새 교사");
        newTeacher.setHashedPassword("newpass");
        newTeacher.setEmail("newteacher@test.com");
        newTeacher.setPhoneNumber("010-9999-9999");
        newTeacher.setRole(UserType.TEACHER);
        newTeacher.setIsEnabled(true);
        newTeacher.setBio("새로운 교사입니다.");

        // Then
        assertEquals(4L, newTeacher.getId());
        assertEquals("새 교사", newTeacher.getName());
        assertEquals("newpass", newTeacher.getHashedPassword());
        assertEquals("newteacher@test.com", newTeacher.getEmail());
        assertEquals("010-9999-9999", newTeacher.getPhoneNumber());
        assertEquals(UserType.TEACHER, newTeacher.getRole());
        assertTrue(newTeacher.getIsEnabled());
        assertEquals("새로운 교사입니다.", newTeacher.getBio());
    }

    @Test
    @DisplayName("Teacher 정보 수정 테스트")
    void testTeacherUpdate() {
        // Given
        Teacher originalTeacher = Teacher.builder()
                .id(6L)
                .name("원본교사")
                .hashedPassword("original123")
                .email("original@test.com")
                .phoneNumber("010-1111-1111")
                .role(UserType.TEACHER)
                .isEnabled(true)
                .bio("원본 소개")
                .build();

        // When
        originalTeacher.setName("수정된교사");
        originalTeacher.setBio("수정된 소개");

        // Then
        assertEquals("수정된교사", originalTeacher.getName());
        assertEquals("수정된 소개", originalTeacher.getBio());
    }

    @Test
    @DisplayName("Teacher 복사 생성 테스트")
    void testTeacherCopy() {
        // Given
        Teacher originalTeacher = Teacher.builder()
                .id(7L)
                .name("원본교사")
                .hashedPassword("copy123")
                .email("copy@test.com")
                .phoneNumber("010-2222-2222")
                .role(UserType.TEACHER)
                .isEnabled(true)
                .bio("원본 소개")
                .build();

        // When
        Teacher copiedTeacher = Teacher.builder()
                .id(8L)
                .name(originalTeacher.getName())
                .hashedPassword(originalTeacher.getHashedPassword())
                .email(originalTeacher.getEmail())
                .phoneNumber(originalTeacher.getPhoneNumber())
                .role(originalTeacher.getRole())
                .isEnabled(originalTeacher.getIsEnabled())
                .bio(originalTeacher.getBio())
                .build();

        // Then
        assertEquals(originalTeacher.getName(), copiedTeacher.getName());
        assertEquals(originalTeacher.getBio(), copiedTeacher.getBio());
        assertNotEquals(originalTeacher.getId(), copiedTeacher.getId());
    }

    @Test
    @DisplayName("Teacher 역할 검증 테스트")
    void testTeacherRoleValidation() {
        // Then
        assertEquals(UserType.TEACHER, teacher.getRole(), "Teacher의 역할은 TEACHER여야 함");
        assertEquals(UserType.TEACHER, teacher2.getRole(), "Teacher의 역할은 TEACHER여야 함");
    }

    @Test
    @DisplayName("Teacher 유효성 검증 예외 테스트")
    void testValidationExceptions() {
        // Given
        Teacher newTeacher = new Teacher();

        // When & Then
        // 역할 검증
        assertThrows(IllegalArgumentException.class, () -> {
            newTeacher.setRole(UserType.STUDENT);
        }, "Teacher의 역할을 STUDENT로 설정할 때 예외가 발생해야 함");

        // 이름 검증
        assertThrows(IllegalArgumentException.class, () -> {
            newTeacher.setName(null);
        }, "이름이 null일 때 예외가 발생해야 함");

        assertThrows(IllegalArgumentException.class, () -> {
            newTeacher.setName("");
        }, "이름이 빈 문자열일 때 예외가 발생해야 함");

        // 이메일 검증
        assertThrows(IllegalArgumentException.class, () -> {
            newTeacher.setEmail("invalid-email");
        }, "유효하지 않은 이메일 형식일 때 예외가 발생해야 함");

        // 전화번호 검증
        assertThrows(IllegalArgumentException.class, () -> {
            newTeacher.setPhoneNumber("123-456-789");
        }, "유효하지 않은 전화번호 형식일 때 예외가 발생해야 함");
    }

    @Test
    @DisplayName("Teacher 편의 메서드 테스트")
    void testConvenienceMethods() {
        // Given
        Teacher testTeacher = Teacher.builder()
                .id(9L)
                .name("테스트교사")
                .hashedPassword("test123")
                .email("test@test.com")
                .phoneNumber("010-3333-3333")
                .role(UserType.TEACHER)
                .isEnabled(true)
                .bio("테스트 소개")
                .build();

        // When & Then
        assertTrue(testTeacher.isActive());

        // 비활성화된 교사
        testTeacher.setIsEnabled(false);
        assertFalse(testTeacher.isActive());
    }

    @Test
    @DisplayName("Teacher UserDetails 인터페이스 구현 테스트")
    void testUserDetailsImplementation() {
        // When & Then
        assertEquals("teacher@test.com", teacher.getUsername());
        assertEquals("password123", teacher.getPassword());
        assertTrue(teacher.isEnabled());
        assertTrue(teacher.isAccountNonExpired());
        assertTrue(teacher.isAccountNonLocked());
        assertTrue(teacher.isCredentialsNonExpired());

        // 권한 확인
        var authorities = teacher.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER")));
    }

    @Test
    @DisplayName("Teacher 편의 메서드 체이닝 테스트")
    void testConvenienceMethodChaining() {
        // Given
        Teacher newTeacher = new Teacher();

        // When
        Teacher result = newTeacher
                .changePhoneNumber("010-9999-9999")
                .changeBio("새로운 소개")
                .changeIsEnabled(false);

        // Then
        assertEquals("010-9999-9999", newTeacher.getPhoneNumber());
        assertEquals("새로운 소개", newTeacher.getBio());
        assertFalse(newTeacher.getIsEnabled());
        assertSame(newTeacher, result);
    }

    @Test
    @DisplayName("Teacher DisplayName 테스트")
    void testDisplayName() {
        // When & Then
        String displayName = teacher.getDisplayName();
        assertNotNull(displayName);
        assertTrue(displayName.contains("김교사"));
        assertTrue(displayName.contains("선생님"));
    }

    @Test
    @DisplayName("Teacher 활성화 상태 변경 테스트")
    void testTeacherActivationStatus() {
        // Given
        Teacher activeTeacher = Teacher.builder()
                .id(10L)
                .name("활성교사")
                .hashedPassword("active123")
                .email("active@test.com")
                .phoneNumber("010-6666-6666")
                .role(UserType.TEACHER)
                .isEnabled(true)
                .build();

        Teacher inactiveTeacher = Teacher.builder()
                .id(11L)
                .name("비활성교사")
                .hashedPassword("inactive123")
                .email("inactive@test.com")
                .phoneNumber("010-7777-7777")
                .role(UserType.TEACHER)
                .isEnabled(false)
                .build();

        // When & Then
        assertTrue(activeTeacher.isActive());
        assertFalse(inactiveTeacher.isActive());

        // 활성화 상태 변경
        activeTeacher.setIsEnabled(false);
        assertFalse(activeTeacher.isActive());

        inactiveTeacher.setIsEnabled(true);
        assertTrue(inactiveTeacher.isActive());
    }

    @Test
    @DisplayName("Teacher 빌더 패턴 다양한 조합 테스트")
    void testTeacherBuilderVariousCombinations() {
        // Given & When
        Teacher teacherWithId = Teacher.builder()
                .id(12L)
                .build();

        Teacher teacherWithName = Teacher.builder()
                .name("이름만교사")
                .build();

        Teacher teacherWithEmail = Teacher.builder()
                .email("email@test.com")
                .build();

        Teacher teacherWithBoth = Teacher.builder()
                .id(13L)
                .name("둘다교사")
                .email("both@test.com")
                .build();

        // Then
        assertEquals(12L, teacherWithId.getId());
        assertNull(teacherWithId.getName());

        assertNull(teacherWithName.getId());
        assertEquals("이름만교사", teacherWithName.getName());

        assertNull(teacherWithEmail.getId());
        assertEquals("email@test.com", teacherWithEmail.getEmail());

        assertEquals(13L, teacherWithBoth.getId());
        assertEquals("둘다교사", teacherWithBoth.getName());
        assertEquals("both@test.com", teacherWithBoth.getEmail());
    }

    @Test
    @DisplayName("Teacher null 값 처리 테스트")
    void testTeacherNullValueHandling() {
        // Given
        Teacher nullTeacher = new Teacher();

        // When & Then
        // 기본값 확인
        assertNull(nullTeacher.getName());
        assertNull(nullTeacher.getEmail());
        assertNull(nullTeacher.getPhoneNumber());
        assertSame(UserType.TEACHER, nullTeacher.getRole());
        assertTrue(nullTeacher.getIsEnabled());
        assertNull(nullTeacher.getBio());

        // null 값으로 설정
        nullTeacher.setBio(null);
        assertNull(nullTeacher.getBio());
//
//        // 빈 문자열 처리
//        nullTeacher.setName("");
//        // 빈 문자열이 설정되면 예외가 발생해야 함
//        assertThrows(IllegalArgumentException.class, nullTeacher.setName(""));
    }
}