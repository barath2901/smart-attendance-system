package com.college.attendance.smart_attendance_server.controller;


import com.college.attendance.smart_attendance_server.model.AttendanceLog;
import com.college.attendance.smart_attendance_server.model.Student;
import com.college.attendance.smart_attendance_server.repository.LogRepository;
import com.college.attendance.smart_attendance_server.repository.StudentRepository;
import com.college.attendance.smart_attendance_server.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Controller
public class DashboardController {

    @Autowired private StudentRepository studentRepo;
    @Autowired private LogRepository logRepo;
    @Autowired private AttendanceService service;

    @GetMapping("/")
    public String home(Model model) {
        // Find all unique classes
        List<String> classes = studentRepo.findAll().stream()
                .map(Student::getClassName).distinct().sorted().toList();

        Map<String, String> classStatus = new HashMap<>();
        for (String c : classes) {
            classStatus.put(c, service.getCurrentSubject(c));
        }

        model.addAttribute("classes", classStatus);
        return "home";
    }

    @GetMapping("/dashboard/{className}")
    public String classDashboard(@PathVariable String className, Model model) {
        List<Student> students = studentRepo.findByClassName(className);
        List<AttendanceLog> logs = logRepo.findByClassName(className);

        // Simple Logic: Get latest log for each student
        Map<Long, AttendanceLog> latestLogs = new HashMap<>();
        for (AttendanceLog log : logs) {
            latestLogs.put(log.getStudentId(), log);
        }

        List<Map<String, String>> rows = new ArrayList<>();
        int presentCount = 0;

        for (Student s : students) {
            Map<String, String> row = new HashMap<>();
            row.put("id", String.valueOf(s.getId()));
            row.put("name", s.getName());

            if (latestLogs.containsKey(s.getId())) {
                AttendanceLog l = latestLogs.get(s.getId());
                row.put("status", l.getStatus());
                row.put("location", l.getLocation());
                row.put("time", l.getTimestamp().toLocalTime().toString().substring(0, 5));
                if (l.getStatus().contains("PRESENT")) presentCount++;
            } else {
                row.put("status", "ABSENT");
                row.put("location", "-");
                row.put("time", "-");
            }
            rows.add(row);
        }

        model.addAttribute("className", className);
        model.addAttribute("subject", service.getCurrentSubject(className));
        model.addAttribute("rows", rows);
        model.addAttribute("presentCount", presentCount);
        model.addAttribute("totalCount", students.size());

        return "class_dashboard";
    }
}