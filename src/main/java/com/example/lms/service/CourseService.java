package com.example.lms.service;

import com.example.lms.dao.CourseRepository;
import com.example.lms.dao.TeacherRepository;
import com.example.lms.dto.CourseCreateDto;
import com.example.lms.dto.CourseDto;
import com.example.lms.mapper.CourseMapper;
import com.example.lms.model.Course;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final CourseMapper courseMapper;

    public Page<CourseDto> getAll(Pageable pageable) {
        return courseRepository.findAll(pageable).map(courseMapper::toDto);
    }

    public CourseDto getById(UUID id) {
        return courseMapper.toDto(getEntityById(id));
    }

    public CourseDto create(CourseCreateDto dto) {
        Course course = courseMapper.toEntity(dto);
        course.setTeacher(teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + dto.getTeacherId())));
        return courseMapper.toDto(courseRepository.save(course));
    }

    public CourseDto update(UUID id, CourseCreateDto dto) {
        Course course = getEntityById(id);
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setTeacher(teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + dto.getTeacherId())));
        return courseMapper.toDto(courseRepository.save(course));
    }

    public void delete(UUID id) {
        courseRepository.deleteById(id);
    }

    private Course getEntityById(UUID id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + id));
    }
}
