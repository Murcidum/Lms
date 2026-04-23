package com.example.lms.controller;

import com.example.lms.dto.ScheduleCreateDto;
import com.example.lms.dto.ScheduleDto;
import com.example.lms.service.ScheduleService;
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

@Tag(name = "Schedules", description = "Управление расписанием")
@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(summary = "Получить всё расписание")
    @ApiResponse(responseCode = "200", description = "Список занятий")
    @GetMapping
    public Page<ScheduleDto> getAll(Pageable pageable) {
        return scheduleService.getAll(pageable);
    }

    @Operation(summary = "Получить занятие по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Занятие найдено"),
            @ApiResponse(responseCode = "404", description = "Занятие не найдено")
    })
    @GetMapping("/{id}")
    public ScheduleDto getById(@PathVariable UUID id) {
        return scheduleService.getById(id);
    }

    @Operation(summary = "Получить расписание группы")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Расписание группы"),
            @ApiResponse(responseCode = "404", description = "Группа не найдена")
    })
    @GetMapping("/group/{groupId}")
    public Page<ScheduleDto> getByGroup(@PathVariable UUID groupId, Pageable pageable) {
        return scheduleService.getByGroup(groupId, pageable);
    }

    @Operation(summary = "Получить расписание преподавателя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Расписание преподавателя"),
            @ApiResponse(responseCode = "404", description = "Преподаватель не найден")
    })
    @GetMapping("/teacher/{teacherId}")
    public Page<ScheduleDto> getByTeacher(@PathVariable UUID teacherId, Pageable pageable) {
        return scheduleService.getByTeacher(teacherId, pageable);
    }

    @Operation(summary = "Создать занятие")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Занятие создано"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Группа, преподаватель или курс не найден"),
            @ApiResponse(responseCode = "409", description = "У преподавателя уже есть занятие в это время")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleDto create(@RequestBody @Valid ScheduleCreateDto dto) {
        return scheduleService.create(dto);
    }

    @Operation(summary = "Обновить занятие")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Занятие обновлено"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Занятие, группа, преподаватель или курс не найден"),
            @ApiResponse(responseCode = "409", description = "У преподавателя уже есть занятие в это время")
    })
    @PutMapping("/{id}")
    public ScheduleDto update(@PathVariable UUID id, @RequestBody @Valid ScheduleCreateDto dto) {
        return scheduleService.update(id, dto);
    }

    @Operation(summary = "Удалить занятие")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Занятие удалено"),
            @ApiResponse(responseCode = "404", description = "Занятие не найдено")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        scheduleService.delete(id);
    }
}
