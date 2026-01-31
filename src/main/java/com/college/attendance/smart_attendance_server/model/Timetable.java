package com.college.attendance.smart_attendance_server.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String className; // "CSE-A"
    private String dayOfWeek; // "Monday"
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private String subject;   // "Maths"
}