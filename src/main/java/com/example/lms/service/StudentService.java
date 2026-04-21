package com.example.lms.service;

import com.example.lms.dao.GroupRepository;
import com.example.lms.dao.StudentRepository;
import com.example.lms.dto.StudentCreateDto;
import com.example.lms.dto.StudentDto;
import com.example.lms.mapper.StudentMapper;
import com.example.lms.model.Student;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final StudentMapper studentMapper;

    public Page<StudentDto> getAll(Pageable pageable) {
        return studentRepository.findAll(pageable).map(studentMapper::toDto);
    }

    public StudentDto getById(UUID id) {
        return studentMapper.toDto(getEntityById(id));
    }

    @Transactional
    public StudentDto create(StudentCreateDto dto) {
        Student student = studentMapper.toEntity(dto);
        student.setGroup(groupRepository.findById(dto.groupId())
                .orElseThrow(() -> new EntityNotFoundException("Group not found: " + dto.groupId())));
        return studentMapper.toDto(studentRepository.save(student));
    }

    @Transactional
    public StudentDto update(UUID id, StudentCreateDto dto) {
        Student student = getEntityById(id);
        student.setName(dto.name());
        student.setSurname(dto.surname());
        student.setGroup(groupRepository.findById(dto.groupId())
                .orElseThrow(() -> new EntityNotFoundException("Group not found: " + dto.groupId())));
        return studentMapper.toDto(studentRepository.save(student));
    }

    public void delete(UUID id) {
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException("Student not found: " + id);
        }
        studentRepository.deleteById(id);
    }

    private Student getEntityById(UUID id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found: " + id));
    }
}
