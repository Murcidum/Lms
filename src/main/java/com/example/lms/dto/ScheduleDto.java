package com.example.lms.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleDto(
        UUID id,
        UUID groupId,
        UUID teacherId,
        UUID courseId,
        LocalDateTime dateTime,
        LocalDateTime endDateTime
) {}
