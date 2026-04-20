package com.example.lms.service;

import com.example.lms.dao.CourseRepository;
import com.example.lms.dao.GroupRepository;
import com.example.lms.dao.ScheduleRepository;
import com.example.lms.dao.TeacherRepository;
import com.example.lms.dto.ScheduleCreateDto;
import com.example.lms.dto.ScheduleDto;
import com.example.lms.mapper.ScheduleMapper;
import com.example.lms.model.Schedule;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final ScheduleMapper scheduleMapper;

    public Page<ScheduleDto> getAll(Pageable pageable) {
        return scheduleRepository.findAll(pageable).map(scheduleMapper::toDto);
    }

    public Page<ScheduleDto> getByGroup(UUID groupId, Pageable pageable) {
        return scheduleRepository.findByGroupId(groupId, pageable).map(scheduleMapper::toDto);
    }

    public Page<ScheduleDto> getByTeacher(UUID teacherId, Pageable pageable) {
        return scheduleRepository.findByTeacherId(teacherId, pageable).map(scheduleMapper::toDto);
    }

    public ScheduleDto getById(UUID id) {
        return scheduleMapper.toDto(getEntityById(id));
    }

    public ScheduleDto create(ScheduleCreateDto dto) {
        checkTeacherConflict(dto.getTeacherId(), dto.getDateTime(), dto.getEndDateTime(), null);
        Schedule schedule = scheduleMapper.toEntity(dto);
        schedule.setGroup(groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Group not found: " + dto.getGroupId())));
        schedule.setTeacher(teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + dto.getTeacherId())));
        schedule.setCourse(courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + dto.getCourseId())));
        return scheduleMapper.toDto(scheduleRepository.save(schedule));
    }

    public ScheduleDto update(UUID id, ScheduleCreateDto dto) {
        checkTeacherConflict(dto.getTeacherId(), dto.getDateTime(), dto.getEndDateTime(), id);
        Schedule schedule = getEntityById(id);
        schedule.setGroup(groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Group not found: " + dto.getGroupId())));
        schedule.setTeacher(teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + dto.getTeacherId())));
        schedule.setCourse(courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + dto.getCourseId())));
        schedule.setDateTime(dto.getDateTime());
        schedule.setEndDateTime(dto.getEndDateTime());
        return scheduleMapper.toDto(scheduleRepository.save(schedule));
    }

    public void delete(UUID id) {
        scheduleRepository.deleteById(id);
    }

    private Schedule getEntityById(UUID id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found: " + id));
    }

    private void checkTeacherConflict(UUID teacherId, LocalDateTime dateTime, LocalDateTime endDateTime, UUID excludeId) {
        boolean conflict = excludeId == null
                ? scheduleRepository.existsTeacherConflict(teacherId, dateTime, endDateTime)
                : scheduleRepository.existsTeacherConflictExcluding(teacherId, dateTime, endDateTime, excludeId);
        if (conflict) {
            throw new IllegalStateException("Teacher already has a class at this time");
        }
    }
}
