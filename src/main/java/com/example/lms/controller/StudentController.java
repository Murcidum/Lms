package com.example.lms.controller;

import com.example.lms.dto.StudentCreateDto;
import com.example.lms.dto.StudentDto;
import com.example.lms.service.StudentService;
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

@Tag(name = "Students", description = "Управление студентами")
@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "Получить всех студентов")
    @ApiResponse(responseCode = "200", description = "Список студентов")
    @GetMapping
    public Page<StudentDto> getAll(Pageable pageable) {
        return studentService.getAll(pageable);
    }

    @Operation(summary = "Получить студента по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Студент найден"),
            @ApiResponse(responseCode = "404", description = "Студент не найден")
    })
    @GetMapping("/{id}")
    public StudentDto getById(@PathVariable UUID id) {
        return studentService.getById(id);
    }

    @Operation(summary = "Создать студента")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Студент создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Группа не найдена")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentDto create(@RequestBody @Valid StudentCreateDto dto) {
        return studentService.create(dto);
    }

    @Operation(summary = "Обновить студента")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Студент обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Студент или группа не найдены")
    })
    @PutMapping("/{id}")
    public StudentDto update(@PathVariable UUID id, @RequestBody @Valid StudentCreateDto dto) {
        return studentService.update(id, dto);
    }

    @Operation(summary = "Удалить студента")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Студент удалён"),
            @ApiResponse(responseCode = "404", description = "Студент не найден")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        studentService.delete(id);
    }
}
