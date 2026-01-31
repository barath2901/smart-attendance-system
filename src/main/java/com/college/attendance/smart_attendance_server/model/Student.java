package com.college.attendance.smart_attendance_server.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Student {
    @Id
    private Long id;       // Roll No (101)
    private String name;   // "Barath"
    private String className; // "CSE-A"
}