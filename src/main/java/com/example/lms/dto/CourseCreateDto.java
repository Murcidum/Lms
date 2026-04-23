package com.example.lms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CourseCreateDto(
        @NotBlank String name,
        @NotBlank String description,
        @NotNull UUID teacherId
) {}
