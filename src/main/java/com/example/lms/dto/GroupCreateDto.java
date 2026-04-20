package com.example.lms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupCreateDto {
    @NotBlank
    String name;
}
