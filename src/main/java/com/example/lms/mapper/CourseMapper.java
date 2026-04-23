package com.example.lms.mapper;

import com.example.lms.dto.CourseCreateDto;
import com.example.lms.dto.CourseDto;
import com.example.lms.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(source = "teacher.id", target = "teacherId")
    CourseDto toDto(Course course);

    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "schedules", ignore = true)
    Course toEntity(CourseCreateDto dto);
}
