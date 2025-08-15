package com.ubbackend.services;

import com.ubbackend.DTOs.StudentDTO;
import com.ubbackend.model.CourseEntity;
import com.ubbackend.model.StudentEntity;

import java.util.Optional;

public interface StudentService {
    Optional<CourseEntity> createStudent(StudentDTO studentDTO) throws Exception;
}
