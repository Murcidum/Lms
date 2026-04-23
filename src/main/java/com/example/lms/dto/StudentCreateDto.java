package com.example.lms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StudentCreateDto(
        @NotBlank String name,
        @NotBlank String surname,
        @NotNull UUID groupId
) {}
