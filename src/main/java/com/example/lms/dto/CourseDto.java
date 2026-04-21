package com.example.lms.dto;

import java.util.UUID;

public record CourseDto(UUID id, String name, String description, UUID teacherId) {}
