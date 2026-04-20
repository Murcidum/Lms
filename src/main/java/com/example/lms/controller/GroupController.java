package com.example.lms.controller;

import com.example.lms.dto.GroupCreateDto;
import com.example.lms.dto.GroupDto;
import com.example.lms.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Groups", description = "Управление группами")
@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "Получить все группы")
    @ApiResponse(responseCode = "200", description = "Список групп")
    @GetMapping
    public Page<GroupDto> getAll(Pageable pageable) {
        return groupService.getAll(pageable);
    }

    @Operation(summary = "Получить группу по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Группа найдена"),
            @ApiResponse(responseCode = "404", description = "Группа не найдена")
    })
    @GetMapping("/{id}")
    public GroupDto getById(@PathVariable UUID id) {
        return groupService.getById(id);
    }

    @Operation(summary = "Создать группу")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Группа создана"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupDto create(@RequestBody @Valid GroupCreateDto dto) {
        return groupService.create(dto);
    }

    @Operation(summary = "Обновить группу")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Группа обновлена"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Группа не найдена")
    })
    @PutMapping("/{id}")
    public GroupDto update(@PathVariable UUID id, @RequestBody @Valid GroupCreateDto dto) {
        return groupService.update(id, dto);
    }

    @Operation(summary = "Удалить группу")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Группа удалена"),
            @ApiResponse(responseCode = "404", description = "Группа не найдена")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        groupService.delete(id);
    }
}
