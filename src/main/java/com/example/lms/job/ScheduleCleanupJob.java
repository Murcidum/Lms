package com.example.lms.job;

import com.example.lms.dao.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ScheduleCleanupJob {
    private final ScheduleRepository scheduleRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteOldSchedules() {
        LocalDateTime date = LocalDateTime.now().minusYears(1);
        scheduleRepository.deleteByDateTimeBefore(date);
    }
}
