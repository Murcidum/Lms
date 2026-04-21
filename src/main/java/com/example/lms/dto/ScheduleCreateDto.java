package com.example.lms.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleCreateDto(
        @NotNull UUID groupId,
        @NotNull UUID teacherId,
        @NotNull UUID courseId,
        @NotNull LocalDateTime dateTime,
        @NotNull LocalDateTime endDateTime
) {
    @AssertTrue(message = "endDateTime must be after dateTime")
    public boolean isEndAfterStart() {
        if (dateTime == null || endDateTime == null) return true;
        return endDateTime.isAfter(dateTime);
    }
}
