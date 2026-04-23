package com.example.lms.dto;

import jakarta.validation.constraints.NotBlank;

public record TeacherCreateDto(@NotBlank String name, @NotBlank String surname) {}
