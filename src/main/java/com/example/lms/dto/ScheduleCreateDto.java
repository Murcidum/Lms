package com.example.lms.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ScheduleCreateDto {
    @NotNull
    UUID groupId;
    @NotNull
    UUID teacherId;
    @NotNull
    UUID courseId;
    @NotNull
    LocalDateTime dateTime;
    @NotNull
    LocalDateTime endDateTime;

    @AssertTrue(message = "endDateTime must be after dateTime")
    public boolean isEndAfterStart() {
        return dateTime != null && endDateTime != null && endDateTime.isAfter(dateTime);
    }
}
