package com.college.attendance.smart_attendance_server.component;



import com.college.attendance.smart_attendance_server.model.Student;
import com.college.attendance.smart_attendance_server.model.Timetable;
import com.college.attendance.smart_attendance_server.repository.StudentRepository;
import com.college.attendance.smart_attendance_server.repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired private StudentRepository studentRepo;
    @Autowired private TimetableRepository timetableRepo;

    @Override
    public void run(String... args) throws Exception {
        loadStudents();
        loadTimetable();
    }

    private void loadStudents() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource("students.csv").getInputStream(), StandardCharsets.UTF_8))) {

            br.mark(1);
            if (br.read() != 0xFEFF) br.reset(); // Skip BOM

            String line; // <--- Defined here
            br.readLine(); // Skip Header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    Student s = new Student();
                    s.setId(Long.parseLong(data[0].trim()));
                    s.setName(data[1].trim());
                    s.setClassName(data[2].trim());
                    studentRepo.save(s);
                }
            }
            System.out.println("✅ Students Loaded.");
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadTimetable() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource("timetable.csv").getInputStream(), StandardCharsets.UTF_8))) {

            String line; // <--- FIX: I added this definition line.
            br.readLine(); // Skip Header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    Timetable t = new Timetable();
                    t.setClassName(data[0].trim());
                    t.setDayOfWeek(data[1].trim());

                    String[] start = data[2].trim().split(":");
                    t.setStartHour(Integer.parseInt(start[0]));
                    t.setStartMinute(Integer.parseInt(start[1]));

                    String[] end = data[3].trim().split(":");
                    t.setEndHour(Integer.parseInt(end[0]));
                    t.setEndMinute(Integer.parseInt(end[1]));

                    t.setSubject(data[4].trim());
                    timetableRepo.save(t);
                }
            }
            System.out.println("✅ Timetable Loaded.");
        } catch (Exception e) { e.printStackTrace(); }
    }
}