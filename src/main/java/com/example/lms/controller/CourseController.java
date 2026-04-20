package com.example.lms.controller;

import com.example.lms.dto.CourseCreateDto;
import com.example.lms.dto.CourseDto;
import com.example.lms.service.CourseService;
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

@Tag(name = "Courses", description = "Управление курсами")
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Получить все курсы")
    @ApiResponse(responseCode = "200", description = "Список курсов")
    @GetMapping
    public Page<CourseDto> getAll(Pageable pageable) {
        return courseService.getAll(pageable);
    }

    @Operation(summary = "Получить курс по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс найден"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @GetMapping("/{id}")
    public CourseDto getById(@PathVariable UUID id) {
        return courseService.getById(id);
    }

    @Operation(summary = "Создать курс")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Курс создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Преподаватель не найден")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDto create(@RequestBody @Valid CourseCreateDto dto) {
        return courseService.create(dto);
    }

    @Operation(summary = "Обновить курс")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Курс или преподаватель не найден")
    })
    @PutMapping("/{id}")
    public CourseDto update(@PathVariable UUID id, @RequestBody @Valid CourseCreateDto dto) {
        return courseService.update(id, dto);
    }

    @Operation(summary = "Удалить курс")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Курс удалён"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        courseService.delete(id);
    }
}
