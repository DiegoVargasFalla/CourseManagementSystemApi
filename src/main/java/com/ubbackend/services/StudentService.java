package com.ubbackend.services;

import com.ubbackend.DTO.*;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Optional<CourseRecursionDTO> createStudent(StudentDTO studentDTO) throws Exception;
    Optional<StudentRecursionDTO> getStudent(Long id) throws Exception;
    List<StudentRecursionDTO> getStudents();
    Optional<StudentRecursionDTO> updateStudent(Long id , StudentUpdateDTO studentUpdateDTO) throws Exception;
    Optional<StudentRecursionDTO> addGradeToStudent(StudentGradeDTO studentGradeDTO) throws Exception;
}
