package com.example.lms.dto;

import java.util.UUID;

public record StudentDto(UUID id, String name, String surname, UUID groupId) {}
