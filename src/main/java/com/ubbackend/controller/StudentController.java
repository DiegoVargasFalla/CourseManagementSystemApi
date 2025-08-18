package com.ubbackend.controller;

import com.ubbackend.DTO.*;
import com.ubbackend.services.StudentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentRecursionDTO>> getStudents() {
        System.out.println("-> at students ##### ");
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudents());
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentRecursionDTO> getStudent(@PathVariable Long id) throws Exception {
        System.out.println("-> at student ##### ");
        Optional<StudentRecursionDTO> studentRecursionExisting = studentService.getStudent(id);
        return studentRecursionExisting
                .map(studentRecursionDTO -> ResponseEntity.status(HttpStatus.OK).body(studentRecursionDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.OK).build());
    }

    @PostMapping("/students")
    public ResponseEntity<CourseRecursionDTO> createStudent(@RequestBody StudentDTO studentDTO) throws Exception {
        Optional<CourseRecursionDTO> courseExisting = studentService.createStudent(studentDTO);
        return courseExisting
                .map(courseRecursionDTO -> ResponseEntity.status(HttpStatus.CREATED).body(courseRecursionDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) throws Exception {
        if(!studentService.deleteStudent(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body("Student deleted successfully");
    }

    @PatchMapping("/students/{id}")
    public ResponseEntity<StudentRecursionDTO> updateStudent(@PathVariable Long id, @RequestBody StudentUpdateDTO studentUpdateDTO) throws Exception {
        Optional<StudentRecursionDTO> studentRecursionExisting = studentService.updateStudent(id, studentUpdateDTO);
        return studentRecursionExisting
        .map(student -> ResponseEntity.status(HttpStatus.OK).body(student))
        .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("students/notes")
    public ResponseEntity<StudentRecursionDTO> addGradeToStudent(@RequestBody StudentGradeDTO studentGradeDTO) throws Exception {

        Optional<StudentRecursionDTO> studentRecursionDTO = studentService.addGradeToStudent(studentGradeDTO);
        return studentRecursionDTO
                .map(recursionDTO -> ResponseEntity.status(HttpStatus.OK).body(recursionDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
