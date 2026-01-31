package com.college.attendance.smart_attendance_server.repository;

import com.college.attendance.smart_attendance_server.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    // FIX: Removed the extra ", Long" inside List<>
    List<Student> findByClassName(String className);
}