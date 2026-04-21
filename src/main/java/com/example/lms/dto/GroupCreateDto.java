package com.example.lms.dto;

import jakarta.validation.constraints.NotBlank;

public record GroupCreateDto(@NotBlank String name) {}
