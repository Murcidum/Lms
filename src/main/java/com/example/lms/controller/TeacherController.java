package com.example.lms.controller;

import com.example.lms.dto.TeacherCreateDto;
import com.example.lms.dto.TeacherDto;
import com.example.lms.service.TeacherService;
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

@Tag(name = "Teachers", description = "Управление преподавателями")
@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @Operation(summary = "Получить всех преподавателей")
    @ApiResponse(responseCode = "200", description = "Список преподавателей")
    @GetMapping
    public Page<TeacherDto> getAll(Pageable pageable) {
        return teacherService.getAll(pageable);
    }

    @Operation(summary = "Получить преподавателя по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Преподаватель найден"),
            @ApiResponse(responseCode = "404", description = "Преподаватель не найден")
    })
    @GetMapping("/{id}")
    public TeacherDto getById(@PathVariable UUID id) {
        return teacherService.getById(id);
    }

    @Operation(summary = "Создать преподавателя")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Преподаватель создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeacherDto create(@RequestBody @Valid TeacherCreateDto dto) {
        return teacherService.create(dto);
    }

    @Operation(summary = "Обновить преподавателя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Преподаватель обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Преподаватель не найден")
    })
    @PutMapping("/{id}")
    public TeacherDto update(@PathVariable UUID id, @RequestBody @Valid TeacherCreateDto dto) {
        return teacherService.update(id, dto);
    }

    @Operation(summary = "Удалить преподавателя")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Преподаватель удалён"),
            @ApiResponse(responseCode = "404", description = "Преподаватель не найден")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        teacherService.delete(id);
    }
}
