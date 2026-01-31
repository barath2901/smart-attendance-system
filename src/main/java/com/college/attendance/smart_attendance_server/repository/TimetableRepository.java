package com.college.attendance.smart_attendance_server.repository;

import com.college.attendance.smart_attendance_server.model.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByClassNameAndDayOfWeek(String className, String dayOfWeek);
}