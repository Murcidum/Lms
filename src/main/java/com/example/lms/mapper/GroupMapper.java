package com.example.lms.mapper;

import com.example.lms.dto.GroupCreateDto;
import com.example.lms.dto.GroupDto;
import com.example.lms.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupDto toDto(Group group);

    @Mapping(target = "students", ignore = true)
    @Mapping(target = "schedules", ignore = true)
    Group toEntity(GroupCreateDto dto);
}
