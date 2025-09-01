package kr.hs.sdh.digitechnetwork.service;

import kr.hs.sdh.digitechnetwork.entity.Student;
import kr.hs.sdh.digitechnetwork.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    @Override
    public List<Student> getAllStudents() {
        return List.of();
    }

    @Override
    public List<Student> getActiveStudents() {
        return List.of();
    }

    @Override
    public long getTotalCount() {
        return 0;
    }

    @Override
    public long getActiveCount() {
        return 0;
    }

    @Override
    public Student updateUserStatus(Long studentId, Boolean isEnabled) {
        return null;
    }
}
