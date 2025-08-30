package kr.hs.sdh.digitechnetwork.entity;

import kr.hs.sdh.digitechnetwork.enums.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Student 엔티티 테스트 클래스
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 */
@DisplayName("Student 엔티티 테스트")
class StudentTest {

    private Student student;
    private Student student2;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .id(1L)
                .name("김학생")
                .hashedPassword("password123")
                .email("student@test.com")
                .phoneNumber("010-1234-5678")
                .role(UserType.STUDENT)
                .isEnabled(true)
                .bio("열심히 공부하는 학생입니다.")
                .grade(1)
                .classroom(1)
                .studentNumber(1)
                .build();

        student2 = Student.builder()
                .id(2L)
                .name("이학생")
                .hashedPassword("password456")
                .email("student2@test.com")
                .phoneNumber("010-2345-6789")
                .role(UserType.STUDENT)
                .isEnabled(true)
                .bio("운동을 좋아하는 학생입니다.")
                .grade(2)
                .classroom(2)
                .studentNumber(2)
                .build();
    }

    @Test
    @DisplayName("Student 빌더 패턴 테스트")
    void testBuilderPattern() {
        // Given & When
        Student newStudent = Student.builder()
                .id(3L)
                .name("박학생")
                .hashedPassword("password789")
                .email("student3@test.com")
                .phoneNumber("010-3456-7890")
                .role(UserType.STUDENT)
                .isEnabled(true)
                .grade(3)
                .classroom(3)
                .studentNumber(3)
                .build();

        // Then
        assertNotNull(newStudent);
        assertEquals(3L, newStudent.getId());
        assertEquals("박학생", newStudent.getName());
        assertEquals("password789", newStudent.getHashedPassword());
        assertEquals("student3@test.com", newStudent.getEmail());
        assertEquals("010-3456-7890", newStudent.getPhoneNumber());
        assertEquals(UserType.STUDENT, newStudent.getRole());
        assertTrue(newStudent.getIsEnabled());
        assertEquals(3, newStudent.getGrade());
        assertEquals(3, newStudent.getClassroom());
        assertEquals(3, newStudent.getStudentNumber());
    }

    @Test
    @DisplayName("Student 기본 생성자 테스트")
    void testDefaultConstructor() {
        // Given & When
        Student newStudent = new Student();

        // Then
        assertNotNull(newStudent);
        assertNull(newStudent.getId());
        assertNull(newStudent.getName());
        assertNull(newStudent.getHashedPassword());
        assertNull(newStudent.getEmail());
        assertNull(newStudent.getPhoneNumber());
        assertSame(UserType.STUDENT, newStudent.getRole());
        assertTrue(newStudent.getIsEnabled());
        assertNull(newStudent.getBio());
        assertNull(newStudent.getGrade());
        assertNull(newStudent.getClassroom());
        assertNull(newStudent.getStudentNumber());
    }

    @Test
    @DisplayName("Student Setter 메서드 테스트")
    void testSetterMethods() {
        // Given
        Student newStudent = new Student();

        // When
        newStudent.setId(4L);
        newStudent.setName("새 학생");
        newStudent.setHashedPassword("newpass");
        newStudent.setEmail("newstudent@test.com");
        newStudent.setPhoneNumber("010-9999-9999");
        newStudent.setRole(UserType.STUDENT);
        newStudent.setIsEnabled(true);
        newStudent.setBio("새로운 학생입니다.");
        newStudent.setGrade(4);
        newStudent.setClassroom(4);
        newStudent.setStudentNumber(4);

        // Then
        assertEquals(4L, newStudent.getId());
        assertEquals("새 학생", newStudent.getName());
        assertEquals("newpass", newStudent.getHashedPassword());
        assertEquals("newstudent@test.com", newStudent.getEmail());
        assertEquals("010-9999-9999", newStudent.getPhoneNumber());
        assertEquals(UserType.STUDENT, newStudent.getRole());
        assertTrue(newStudent.getIsEnabled());
        assertEquals("새로운 학생입니다.", newStudent.getBio());
        assertEquals(4, newStudent.getGrade());
        assertEquals(4, newStudent.getClassroom());
        assertEquals(4, newStudent.getStudentNumber());
    }

    @Test
    @DisplayName("Student 학년 변경 테스트")
    void testGradeChange() {
        // Given
        Integer originalGrade = student.getGrade();

        // When
        student.setGrade(2);

        // Then
        assertNotEquals(originalGrade, student.getGrade());
        assertEquals(2, student.getGrade());
    }

    @Test
    @DisplayName("Student 반 변경 테스트")
    void testClassroomChange() {
        // Given
        Integer originalClassroom = student.getClassroom();

        // When
        student.setClassroom(2);

        // Then
        assertNotEquals(originalClassroom, student.getClassroom());
        assertEquals(2, student.getClassroom());
    }

    @Test
    @DisplayName("Student 학번 변경 테스트")
    void testStudentNumberChange() {
        // Given
        Integer originalStudentNumber = student.getStudentNumber();

        // When
        student.setStudentNumber(2);

        // Then
        assertNotEquals(originalStudentNumber, student.getStudentNumber());
        assertEquals(2, student.getStudentNumber());
    }

    @Test
    @DisplayName("Student 유효성 검증 테스트")
    void testStudentValidation() {
        // Then
        assertNotNull(student.getId(), "ID는 null이 아니어야 함");
        assertNotNull(student.getName(), "이름은 null이 아니어야 함");
        assertNotNull(student.getHashedPassword(), "비밀번호는 null이 아니어야 함");
        assertNotNull(student.getEmail(), "이메일은 null이 아니어야 함");
        assertNotNull(student.getPhoneNumber(), "전화번호는 null이 아니어야 함");
        assertNotNull(student.getRole(), "역할은 null이 아니어야 함");
        assertNotNull(student.getIsEnabled(), "활성화 상태는 null이 아니어야 함");
        assertNotNull(student.getGrade(), "학년은 null이 아니어야 함");
        assertNotNull(student.getClassroom(), "반은 null이 아니어야 함");
        assertNotNull(student.getStudentNumber(), "학번은 null이 아니어야 함");
    }

    @Test
    @DisplayName("Student 학년 범위 테스트")
    void testGradeRange() {
        // Given & When & Then
        // 학년은 일반적으로 1~6학년 (초등학교) 또는 1~3학년 (중학교) 또는 1~3학년 (고등학교)
        assertTrue(student.getGrade() >= 1, "학년은 1 이상이어야 함");
        assertTrue(student.getGrade() <= 6, "학년은 6 이하여야 함");

        assertTrue(student2.getGrade() >= 1, "학년은 1 이상이어야 함");
        assertTrue(student2.getGrade() <= 6, "학년은 6 이하여야 함");
    }

    @Test
    @DisplayName("Student 반 범위 테스트")
    void testClassroomRange() {
        // Given & When & Then
        // 반은 일반적으로 1~20반 정도
        assertTrue(student.getClassroom() >= 1, "반은 1 이상이어야 함");
        assertTrue(student.getClassroom() <= 20, "반은 20 이하여야 함");

        assertTrue(student2.getClassroom() >= 1, "반은 1 이상이어야 함");
        assertTrue(student2.getClassroom() <= 20, "반은 20 이하여야 함");
    }

    @Test
    @DisplayName("Student 학번 범위 테스트")
    void testStudentNumberRange() {
        // Given & When & Then
        // 학번은 일반적으로 1~40번 정도
        assertTrue(student.getStudentNumber() >= 1, "학번은 1 이상이어야 함");
        assertTrue(student.getStudentNumber() <= 40, "학번은 40 이하여야 함");

        assertTrue(student2.getStudentNumber() >= 1, "학번은 1 이상이어야 함");
        assertTrue(student2.getStudentNumber() <= 40, "학번은 40 이하여야 함");
    }

    @Test
    @DisplayName("Student 정보 수정 테스트")
    void testStudentUpdate() {
        // Given
        Student originalStudent = Student.builder()
                .id(6L)
                .name("원본학생")
                .hashedPassword("original123")
                .email("original@test.com")
                .phoneNumber("010-1111-1111")
                .role(UserType.STUDENT)
                .isEnabled(true)
                .grade(1)
                .classroom(1)
                .studentNumber(1)
                .build();

        // When
        originalStudent.setName("수정된학생");
        originalStudent.setGrade(2);
        originalStudent.setClassroom(3);
        originalStudent.setStudentNumber(5);

        // Then
        assertEquals("수정된학생", originalStudent.getName());
        assertEquals(2, originalStudent.getGrade());
        assertEquals(3, originalStudent.getClassroom());
        assertEquals(5, originalStudent.getStudentNumber());
    }

    @Test
    @DisplayName("Student 복사 생성 테스트")
    void testStudentCopy() {
        // Given
        Student originalStudent = Student.builder()
                .id(7L)
                .name("원본학생")
                .hashedPassword("copy123")
                .email("copy@test.com")
                .phoneNumber("010-2222-2222")
                .role(UserType.STUDENT)
                .isEnabled(true)
                .grade(1)
                .classroom(1)
                .studentNumber(1)
                .build();

        // When
        Student copiedStudent = Student.builder()
                .id(8L)
                .name(originalStudent.getName())
                .hashedPassword(originalStudent.getHashedPassword())
                .email(originalStudent.getEmail())
                .phoneNumber(originalStudent.getPhoneNumber())
                .role(originalStudent.getRole())
                .isEnabled(originalStudent.getIsEnabled())
                .grade(originalStudent.getGrade())
                .classroom(originalStudent.getClassroom())
                .studentNumber(originalStudent.getStudentNumber())
                .build();

        // Then
        assertEquals(originalStudent.getName(), copiedStudent.getName());
        assertEquals(originalStudent.getGrade(), copiedStudent.getGrade());
        assertEquals(originalStudent.getClassroom(), copiedStudent.getClassroom());
        assertEquals(originalStudent.getStudentNumber(), copiedStudent.getStudentNumber());
        assertNotEquals(originalStudent.getId(), copiedStudent.getId());
    }

    @Test
    @DisplayName("Student 역할 검증 테스트")
    void testStudentRoleValidation() {
        // Then
        assertEquals(UserType.STUDENT, student.getRole(), "Student의 역할은 STUDENT여야 함");
        assertEquals(UserType.STUDENT, student2.getRole(), "Student의 역할은 STUDENT여야 함");
    }

    @Test
    @DisplayName("Student 체이닝 메서드 테스트")
    void testChainingMethods() {
        // Given
        Student newStudent = new Student();

        // When
        Student result = newStudent
                .changeGrade(3)
                .changeClassroom(2)
                .changeStudentNumber(15);

        // Then
        assertEquals(3, newStudent.getGrade());
        assertEquals(2, newStudent.getClassroom());
        assertEquals(15, newStudent.getStudentNumber());
        assertSame(newStudent, result); // 체이닝 결과가 같은 객체인지 확인
    }

    @Test
    @DisplayName("Student 유효성 검증 예외 테스트")
    void testValidationExceptions() {
        // Given
        Student newStudent = new Student();

        // When & Then
        // 학년 범위 검증
        assertThrows(IllegalArgumentException.class, () -> {
            newStudent.setGrade(0);
        }, "학년이 0일 때 예외가 발생해야 함");

        assertThrows(IllegalArgumentException.class, () -> {
            newStudent.setGrade(7);
        }, "학년이 7일 때 예외가 발생해야 함");

        // 반 범위 검증
        assertThrows(IllegalArgumentException.class, () -> {
            newStudent.setClassroom(0);
        }, "반이 0일 때 예외가 발생해야 함");

        assertThrows(IllegalArgumentException.class, () -> {
            newStudent.setClassroom(21);
        }, "반이 21일 때 예외가 발생해야 함");

        // 학번 범위 검증
        assertThrows(IllegalArgumentException.class, () -> {
            newStudent.setStudentNumber(0);
        }, "학번이 0일 때 예외가 발생해야 함");

        assertThrows(IllegalArgumentException.class, () -> {
            newStudent.setStudentNumber(51);
        }, "학번이 51일 때 예외가 발생해야 함");

        // 역할 검증
        assertThrows(IllegalArgumentException.class, () -> {
            newStudent.setRole(UserType.TEACHER);
        }, "Student의 역할을 TEACHER로 설정할 때 예외가 발생해야 함");

        // 이름 검증
        assertThrows(IllegalArgumentException.class, () -> {
            newStudent.setName(null);
        }, "이름이 null일 때 예외가 발생해야 함");

        assertThrows(IllegalArgumentException.class, () -> {
            newStudent.setName("");
        }, "이름이 빈 문자열일 때 예외가 발생해야 함");

        // 이메일 검증
        assertThrows(IllegalArgumentException.class, () -> {
            newStudent.setEmail("invalid-email");
        }, "유효하지 않은 이메일 형식일 때 예외가 발생해야 함");

        // 전화번호 검증
        assertThrows(IllegalArgumentException.class, () -> {
            newStudent.setPhoneNumber("123-456-789");
        }, "유효하지 않은 전화번호 형식일 때 예외가 발생해야 함");
    }

    @Test
    @DisplayName("Student 편의 메서드 테스트")
    void testConvenienceMethods() {
        // Given
        Student testStudent = Student.builder()
                .id(9L)
                .name("테스트학생")
                .hashedPassword("test123")
                .email("test@test.com")
                .phoneNumber("010-3333-3333")
                .role(UserType.STUDENT)
                .isEnabled(true)
                .grade(2)
                .classroom(3)
                .studentNumber(15)
                .build();

        // When & Then
        assertEquals("2학년 3반 15번", testStudent.getFullClassInfo());

        // 같은 반 학생과의 비교
        Student sameClassStudent = Student.builder()
                .id(10L)
                .name("같은반학생")
                .hashedPassword("same123")
                .email("same@test.com")
                .phoneNumber("010-4444-4444")
                .role(UserType.STUDENT)
                .isEnabled(true)
                .grade(2)
                .classroom(3)
                .studentNumber(20)
                .build();

        assertTrue(testStudent.isInSameClass(sameClassStudent));

        // 다른 반 학생과의 비교
        Student differentClassStudent = Student.builder()
                .id(11L)
                .name("다른반학생")
                .hashedPassword("different123")
                .email("different@test.com")
                .phoneNumber("010-5555-5555")
                .role(UserType.STUDENT)
                .isEnabled(true)
                .grade(2)
                .classroom(4)
                .studentNumber(15)
                .build();

        assertFalse(testStudent.isInSameClass(differentClassStudent));
    }

    @Test
    @DisplayName("Student UserDetails 인터페이스 구현 테스트")
    void testUserDetailsImplementation() {
        // When & Then
        assertEquals("student@test.com", student.getUsername());
        assertEquals("password123", student.getPassword());
        assertTrue(student.isEnabled());
        assertTrue(student.isAccountNonExpired());
        assertTrue(student.isAccountNonLocked());
        assertTrue(student.isCredentialsNonExpired());

        // 권한 확인
        var authorities = student.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT")));
    }

    @Test
    @DisplayName("Student 편의 메서드 체이닝 테스트")
    void testConvenienceMethodChaining() {
        // Given
        Student newStudent = new Student();

        // When
        Student result = newStudent
                .changePhoneNumber("010-9999-9999")
                .changeBio("새로운 소개")
                .changeIsEnabled(false);

        // Then
        assertEquals("010-9999-9999", newStudent.getPhoneNumber());
        assertEquals("새로운 소개", newStudent.getBio());
        assertFalse(newStudent.getIsEnabled());
        assertSame(newStudent, result);
    }

    @Test
    @DisplayName("Student DisplayName 테스트")
    void testDisplayName() {
        // When & Then
        String displayName = student.getDisplayName();
        assertNotNull(displayName);
        assertTrue(displayName.contains("김학생"));
        assertTrue(displayName.contains("STUDENT"));
    }
}