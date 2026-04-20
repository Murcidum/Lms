package com.example.lms.mapper;

import com.example.lms.dto.ScheduleCreateDto;
import com.example.lms.dto.ScheduleDto;
import com.example.lms.model.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "teacher.id", target = "teacherId")
    @Mapping(source = "course.id", target = "courseId")
    ScheduleDto toDto(Schedule schedule);

    @Mapping(target = "group", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "course", ignore = true)
    Schedule toEntity(ScheduleCreateDto dto);
}
