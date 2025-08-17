package com.ubbackend.services;

import com.ubbackend.DTOs.CourseDTO;
import com.ubbackend.DTOs.CourseRecursionDTO;
import com.ubbackend.DTOs.CourseUpdateDTO;
import com.ubbackend.DTOs.NewStudentDTO;
import com.ubbackend.model.CourseEntity;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<CourseRecursionDTO> getCourses();
    Optional<CourseRecursionDTO> getCourse(Long id) throws Exception;
    boolean createCourse(CourseDTO courseDTO) throws Exception;
    void deleteCourse(Long id) throws Exception;
    void updateCourse(CourseUpdateDTO courseUpdateDTO) throws Exception;
    Optional<CourseEntity> newStudent(NewStudentDTO newStudentDTO) throws Exception;
    boolean deleteStudentFromCourse(NewStudentDTO studentDTO);
}
