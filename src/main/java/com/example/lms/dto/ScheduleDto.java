package com.example.lms.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ScheduleDto {
    UUID id;
    UUID groupId;
    UUID teacherId;
    UUID courseId;
    LocalDateTime dateTime;
    LocalDateTime endDateTime;
}
