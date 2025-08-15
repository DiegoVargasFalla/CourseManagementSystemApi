package com.ubbackend.services;

<<<<<<< HEAD
import java.util.List;

import com.ubbackend.model.Student;

public interface StudentService {
    String proof();
    List<Student> getStudents();
=======
import com.ubbackend.DTOs.StudentDTO;
import com.ubbackend.model.CourseEntity;
import com.ubbackend.model.StudentEntity;

import java.util.Optional;

public interface StudentService {
    Optional<CourseEntity> createStudent(StudentDTO studentDTO) throws Exception;
>>>>>>> bf6cd4ede20fecbb5e96adf805239d63d3d30464
}
