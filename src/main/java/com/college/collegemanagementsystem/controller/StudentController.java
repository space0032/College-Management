package com.college.collegemanagementsystem.controller;

import com.college.collegemanagementsystem.entity.Student;
import com.college.collegemanagementsystem.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }
    
    @PostMapping
    public ResponseEntity<?> createStudent(@Valid @RequestBody Student student, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation failed");
        }
        return ResponseEntity.ok(studentService.saveStudent(student));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @Valid @RequestBody Student student, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation failed");
        }
        student.setId(id);
        return ResponseEntity.ok(studentService.saveStudent(student));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
}