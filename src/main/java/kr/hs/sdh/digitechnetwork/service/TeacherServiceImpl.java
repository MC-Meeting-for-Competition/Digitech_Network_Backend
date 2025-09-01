package kr.hs.sdh.digitechnetwork.service;

import kr.hs.sdh.digitechnetwork.entity.Teacher;
import kr.hs.sdh.digitechnetwork.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public List<Teacher> getActiveTeachers() {
        return teacherRepository.findActiveUsers();
    }

    @Override
    public long getTotalCount() {
        return teacherRepository.count();
    }

    @Override
    public long getActiveCount() {
        return teacherRepository.countActiveUsers();
    }

    @Override
    public Teacher updateUserStatus(Long teacherId, Boolean isEnabled) {
        return null;
    }
}
