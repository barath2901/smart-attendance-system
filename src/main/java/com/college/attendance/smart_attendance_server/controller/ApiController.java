package com.college.attendance.smart_attendance_server.controller;


import com.college.attendance.smart_attendance_server.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class ApiController {

    @Autowired private AttendanceService service;

    @PostMapping("/scan")
    public String receiveScan(@RequestBody Map<String, Object> payload) {
        try {
            Long studentId = Long.valueOf(payload.get("studentId").toString());
            String nodeName = (String) payload.get("nodeName");
            service.processScan(studentId, nodeName);
            return "OK";
        } catch (Exception e) {
            return "Error";
        }
    }
}