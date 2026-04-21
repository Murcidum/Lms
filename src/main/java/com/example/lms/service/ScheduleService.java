package com.example.lms.service;

import com.example.lms.dao.CourseRepository;
import com.example.lms.dao.GroupRepository;
import com.example.lms.dao.ScheduleRepository;
import com.example.lms.dao.TeacherRepository;
import com.example.lms.dto.ScheduleCreateDto;
import com.example.lms.dto.ScheduleDto;
import com.example.lms.mapper.ScheduleMapper;
import com.example.lms.model.Schedule;
import com.example.lms.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public ScheduleDto create(ScheduleCreateDto dto) {
        checkTeacherConflict(dto.teacherId(), dto.dateTime(), dto.endDateTime(), null);
        checkGroupConflict(dto.groupId(), dto.dateTime(), dto.endDateTime(), null);
        Schedule schedule = scheduleMapper.toEntity(dto);
        schedule.setGroup(groupRepository.findById(dto.groupId())
                .orElseThrow(() -> new NotFoundException("Group not found: " + dto.groupId())));
        schedule.setTeacher(teacherRepository.findById(dto.teacherId())
                .orElseThrow(() -> new NotFoundException("Teacher not found: " + dto.teacherId())));
        schedule.setCourse(courseRepository.findById(dto.courseId())
                .orElseThrow(() -> new NotFoundException("Course not found: " + dto.courseId())));
        return scheduleMapper.toDto(scheduleRepository.save(schedule));
    }

    @Transactional
    public ScheduleDto update(UUID id, ScheduleCreateDto dto) {
        checkTeacherConflict(dto.teacherId(), dto.dateTime(), dto.endDateTime(), id);
        checkGroupConflict(dto.groupId(), dto.dateTime(), dto.endDateTime(), id);
        Schedule schedule = getEntityById(id);
        schedule.setGroup(groupRepository.findById(dto.groupId())
                .orElseThrow(() -> new NotFoundException("Group not found: " + dto.groupId())));
        schedule.setTeacher(teacherRepository.findById(dto.teacherId())
                .orElseThrow(() -> new NotFoundException("Teacher not found: " + dto.teacherId())));
        schedule.setCourse(courseRepository.findById(dto.courseId())
                .orElseThrow(() -> new NotFoundException("Course not found: " + dto.courseId())));
        schedule.setDateTime(dto.dateTime());
        schedule.setEndDateTime(dto.endDateTime());
        return scheduleMapper.toDto(scheduleRepository.save(schedule));
    }

    public void delete(UUID id) {
        if (!scheduleRepository.existsById(id)) {
            throw new NotFoundException("Schedule not found: " + id);
        }
        scheduleRepository.deleteById(id);
    }

    private Schedule getEntityById(UUID id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Schedule not found: " + id));
    }

    private void checkTeacherConflict(UUID teacherId, LocalDateTime dateTime, LocalDateTime endDateTime, UUID excludeId) {
        boolean conflict = excludeId == null
                ? scheduleRepository.existsTeacherConflict(teacherId, dateTime, endDateTime)
                : scheduleRepository.existsTeacherConflictExcluding(teacherId, dateTime, endDateTime, excludeId);
        if (conflict) {
            throw new IllegalStateException("Teacher already has a class at this time");
        }
    }

    private void checkGroupConflict(UUID groupId, LocalDateTime dateTime, LocalDateTime endDateTime, UUID excludeId) {
        boolean conflict = excludeId == null
                ? scheduleRepository.existsGroupConflict(groupId, dateTime, endDateTime)
                : scheduleRepository.existsGroupConflictExcluding(groupId, dateTime, endDateTime, excludeId);
        if (conflict) {
            throw new IllegalStateException("Group already has a class at this time");
        }
    }
}
