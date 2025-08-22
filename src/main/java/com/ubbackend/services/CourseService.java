package com.ubbackend.services;

import com.ubbackend.DTO.CourseDTO;
import com.ubbackend.DTO.CourseRecursionDTO;
import com.ubbackend.DTO.CourseUpdateDTO;
import com.ubbackend.DTO.NewStudentDTO;
import com.ubbackend.model.CourseEntity;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<CourseRecursionDTO> getCourses();
    Optional<CourseRecursionDTO> getCourse(Long id);
    Optional<CourseEntity> createCourse(CourseDTO courseDTO) throws Exception;
    boolean deleteCourse(Long id) throws Exception;
    Optional<CourseEntity> updateCourse(CourseUpdateDTO courseUpdateDTO) throws Exception;
    Optional<CourseRecursionDTO> newStudent(NewStudentDTO newStudentDTO) throws Exception;
    Optional<CourseRecursionDTO> deleteStudentFromCourse(NewStudentDTO studentDTO);
}
