package com.example.lms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class StudentCreateDto {
    @NotBlank
    String name;
    @NotBlank
    String surname;
    @NotNull
    UUID groupId;
}
