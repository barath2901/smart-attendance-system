package com.college.attendance.smart_attendance_server.repository;


import com.college.attendance.smart_attendance_server.model.AttendanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LogRepository extends JpaRepository<AttendanceLog, Long> {
    // Find logs for a specific class
    List<AttendanceLog> findByClassName(String className);
}