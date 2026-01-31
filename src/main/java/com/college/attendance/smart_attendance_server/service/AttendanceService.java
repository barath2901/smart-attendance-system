package com.college.attendance.smart_attendance_server.service;


import com.college.attendance.smart_attendance_server.model.AttendanceLog;
import com.college.attendance.smart_attendance_server.model.Student;
import com.college.attendance.smart_attendance_server.model.Timetable;
import com.college.attendance.smart_attendance_server.repository.LogRepository;
import com.college.attendance.smart_attendance_server.repository.StudentRepository;
import com.college.attendance.smart_attendance_server.repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired private LogRepository logRepo;
    @Autowired private StudentRepository studentRepo;
    @Autowired private TimetableRepository timetableRepo;

    private static final List<String> SAFE_ZONES = List.of("Classroom", "Library", "Lab 1", "Seminar Hall");

    public void processScan(Long studentId, String location) {
        Student student = studentRepo.findById(studentId).orElse(null);
        if (student == null) return;

        AttendanceLog log = new AttendanceLog();
        log.setStudentId(studentId);
        log.setStudentName(student.getName());
        log.setClassName(student.getClassName());
        log.setLocation(location);
        log.setStatus(SAFE_ZONES.contains(location) ? "PRESENT" : "WARNING (Bunking)");
        log.setTimestamp(LocalDateTime.now());

        // This is a simplified logic: In real app, update existing record instead of inserting new
        logRepo.save(log);
    }

    public String getCurrentSubject(String className) {
        LocalDateTime now = LocalDateTime.now();
        String day = now.getDayOfWeek().toString(); // e.g. MONDAY
        // Convert Java Day (MONDAY) to CSV Day (Monday)
        String dayFormatted = day.charAt(0) + day.substring(1).toLowerCase();

        List<Timetable> periods = timetableRepo.findByClassNameAndDayOfWeek(className, dayFormatted);

        int currentMin = now.getHour() * 60 + now.getMinute();

        for (Timetable t : periods) {
            int startMin = t.getStartHour() * 60 + t.getStartMinute();
            int endMin = t.getEndHour() * 60 + t.getEndMinute();

            if (currentMin >= startMin && currentMin < endMin) {
                return t.getSubject();
            }
        }
        return "Free Period";
    }
}