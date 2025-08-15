package com.ubbackend.services;

<<<<<<< HEAD
import java.util.ArrayList;

import com.ubbackend.model.Course;

public interface CourseService {
    //ArrayList<Course> getCourses();
=======
import com.ubbackend.DTOs.CourseDTO;
import com.ubbackend.DTOs.CourseUpdateDTO;
import com.ubbackend.DTOs.NewStudentDTO;
import com.ubbackend.model.CourseEntity;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<CourseEntity> getCourses();
    Optional<CourseEntity> getCourse(Long id);
    boolean createCourse(CourseDTO courseDTO) throws Exception;
    void deleteCourse(Long id) throws Exception;
    void updateCourse(CourseUpdateDTO courseUpdateDTO) throws Exception;
    Optional<CourseEntity> newStudent(NewStudentDTO newStudentDTO) throws Exception;
>>>>>>> bf6cd4ede20fecbb5e96adf805239d63d3d30464
}
