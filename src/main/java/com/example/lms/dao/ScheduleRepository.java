package com.example.lms.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import com.example.lms.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    Page<Schedule> findByGroupId(UUID groupId, Pageable pageable);
    Page<Schedule> findByTeacherId(UUID teacherId, Pageable pageable);
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Schedule s WHERE s.teacher.id = :teacherId AND s.dateTime < :endDateTime AND s.endDateTime > :dateTime")
    boolean existsTeacherConflict(@Param("teacherId") UUID teacherId, @Param("dateTime") LocalDateTime dateTime, @Param("endDateTime") LocalDateTime endDateTime);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Schedule s WHERE s.teacher.id = :teacherId AND s.dateTime < :endDateTime AND s.endDateTime > :dateTime AND s.id <> :excludeId")
    boolean existsTeacherConflictExcluding(@Param("teacherId") UUID teacherId, @Param("dateTime") LocalDateTime dateTime, @Param("endDateTime") LocalDateTime endDateTime, @Param("excludeId") UUID excludeId);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Schedule s WHERE s.group.id = :groupId AND s.dateTime < :endDateTime AND s.endDateTime > :dateTime")
    boolean existsGroupConflict(@Param("groupId") UUID groupId, @Param("dateTime") LocalDateTime dateTime, @Param("endDateTime") LocalDateTime endDateTime);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Schedule s WHERE s.group.id = :groupId AND s.dateTime < :endDateTime AND s.endDateTime > :dateTime AND s.id <> :excludeId")
    boolean existsGroupConflictExcluding(@Param("groupId") UUID groupId, @Param("dateTime") LocalDateTime dateTime, @Param("endDateTime") LocalDateTime endDateTime, @Param("excludeId") UUID excludeId);

}

