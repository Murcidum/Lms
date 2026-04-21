package com.example.lms.service;

import com.example.lms.dao.TeacherRepository;
import com.example.lms.dto.TeacherCreateDto;
import com.example.lms.dto.TeacherDto;
import com.example.lms.mapper.TeacherMapper;
import com.example.lms.model.Teacher;
import com.example.lms.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    public Page<TeacherDto> getAll(Pageable pageable) {
        return teacherRepository.findAll(pageable).map(teacherMapper::toDto);
    }

    public TeacherDto getById(UUID id) {
        return teacherMapper.toDto(getEntityById(id));
    }

    public TeacherDto create(TeacherCreateDto dto) {
        return teacherMapper.toDto(teacherRepository.save(teacherMapper.toEntity(dto)));
    }

    public TeacherDto update(UUID id, TeacherCreateDto dto) {
        Teacher teacher = getEntityById(id);
        teacher.setName(dto.name());
        teacher.setSurname(dto.surname());
        return teacherMapper.toDto(teacherRepository.save(teacher));
    }


    public void delete(UUID id) {
        if (!teacherRepository.existsById(id)) {
            throw new NotFoundException("Teacher not found: " + id);
        }
        teacherRepository.deleteById(id);
    }

    private Teacher getEntityById(UUID id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Teacher not found: " + id));
    }

}
