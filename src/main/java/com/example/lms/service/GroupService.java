package com.example.lms.service;

import com.example.lms.dao.GroupRepository;
import com.example.lms.dto.GroupCreateDto;
import com.example.lms.dto.GroupDto;
import com.example.lms.mapper.GroupMapper;
import com.example.lms.model.Group;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public Page<GroupDto> getAll(Pageable pageable) {
        return groupRepository.findAll(pageable).map(groupMapper::toDto);
    }

    public GroupDto getById(UUID id) {
        return groupMapper.toDto(getEntityById(id));
    }

    public GroupDto create(GroupCreateDto dto) {
        return groupMapper.toDto(groupRepository.save(groupMapper.toEntity(dto)));
    }

    public GroupDto update(UUID id, GroupCreateDto dto) {
        Group group = getEntityById(id);
        group.setName(dto.getName());
        return groupMapper.toDto(groupRepository.save(group));
    }

    public void delete(UUID id) {
        groupRepository.deleteById(id);
    }

    private Group getEntityById(UUID id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group not found: " + id));
    }
}
