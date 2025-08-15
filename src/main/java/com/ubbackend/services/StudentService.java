package com.ubbackend.services;

import com.ubbackend.DTOs.StudentDTO;
import com.ubbackend.DTOs.StudentUpdateDTO;
import com.ubbackend.model.CourseEntity;
import com.ubbackend.model.StudentEntity;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Optional<CourseEntity> createStudent(StudentDTO studentDTO) throws Exception;
    //Optional<StudentEntity> getStudent(Long dni) throws Exception;
    List<StudentEntity> getStudents();
    //boolean deleteStudent(Long dni) throws Exception;
    //Optional<StudentEntity> updateStudent(StudentUpdateDTO studentUpdateDTO);
}
