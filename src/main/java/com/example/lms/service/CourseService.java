package com.example.lms.service;

import com.example.lms.dao.CourseRepository;
import com.example.lms.dao.TeacherRepository;
import com.example.lms.dto.CourseCreateDto;
import com.example.lms.dto.CourseDto;
import com.example.lms.mapper.CourseMapper;
import com.example.lms.model.Course;
import com.example.lms.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public CourseDto create(CourseCreateDto dto) {
        Course course = courseMapper.toEntity(dto);
        course.setTeacher(teacherRepository.findById(dto.teacherId())
                .orElseThrow(() -> new NotFoundException("Teacher not found: " + dto.teacherId())));
        return courseMapper.toDto(courseRepository.save(course));
    }

    @Transactional
    public CourseDto update(UUID id, CourseCreateDto dto) {
        Course course = getEntityById(id);
        course.setName(dto.name());
        course.setDescription(dto.description());
        course.setTeacher(teacherRepository.findById(dto.teacherId())
                .orElseThrow(() -> new NotFoundException("Teacher not found: " + dto.teacherId())));
        return courseMapper.toDto(courseRepository.save(course));
    }

    public void delete(UUID id) {
        if (!courseRepository.existsById(id)) {
            throw new NotFoundException("Course not found: " + id);
        }
        courseRepository.deleteById(id);
    }

    private Course getEntityById(UUID id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found: " + id));
    }
}
