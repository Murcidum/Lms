package com.example.lms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherCreateDto {
    @NotBlank
    String name;
    @NotBlank
    String surname;
}
