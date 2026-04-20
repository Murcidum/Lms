package com.example.lms.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TeacherDto {
    UUID id;
    String name;
    String surname;
}
