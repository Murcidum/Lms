package com.example.lms.mapper;

import com.example.lms.dto.TeacherCreateDto;
import com.example.lms.dto.TeacherDto;
import com.example.lms.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    TeacherDto toDto(Teacher teacher);

    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "schedules", ignore = true)
    Teacher toEntity(TeacherCreateDto dto);
}

