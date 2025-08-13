package com.ubbackend.controller;

import com.ubbackend.model.Student;
import com.ubbackend.services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/proof")
    public ResponseEntity<String> proofEndPoint() {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.proof());
    }

    @GetMapping("Students")
    public ResponseEntity<List<Student>> getStudents() {
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(new Student());
    }

    @PostMapping("/create/student")
    public ResponseEntity<?> createStudent(@RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete/student/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/update/student")
    public ResponseEntity<?> updateStudent(@RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
