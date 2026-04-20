package com.example.lms.mapper;

import com.example.lms.dto.StudentCreateDto;
import com.example.lms.dto.StudentDto;
import com.example.lms.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(source = "group.id", target = "groupId")
    StudentDto toDto(Student student);

    @Mapping(target = "group", ignore = true)
    Student toEntity(StudentCreateDto dto);
}
