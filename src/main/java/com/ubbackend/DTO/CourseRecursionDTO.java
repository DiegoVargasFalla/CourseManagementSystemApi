package com.ubbackend.DTO;

import com.ubbackend.model.CourseEntity;
import com.ubbackend.model.GradeEntity;
import com.ubbackend.model.StudentEntity;

import java.util.HashSet;
import java.util.Set;

public class CourseRecursionDTO {

    private Long id;

    private String name;

    private float average;

    private Set<StudentRecursionDTO> students = new HashSet<>();

    private Set<GradeRecursionDTO> grades = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public Set<GradeRecursionDTO> getGrades() {
        return grades;
    }

    public void setGrades(Set<GradeRecursionDTO> grades) {
        this.grades = grades;
    }

    public Set<StudentRecursionDTO> getStudents() {
        return students;
    }

    public void setStudents(Set<StudentRecursionDTO> students) {
        this.students = students;
    }

    public void toCourseRecursionDTO(CourseEntity courseEntity) {

        this.id = courseEntity.getId();
        this.name = courseEntity.getName();
        this.average = courseEntity.getAverage();

        for(GradeEntity gradeEntity : courseEntity.getGrades()) {
            GradeRecursionDTO gradeRecursionDTO = toGradeRecursionDTO(gradeEntity);
            this.grades.add(gradeRecursionDTO);
        }

        for(StudentEntity studentEntity : courseEntity.getStudents()) {
            StudentRecursionDTO studentRecursionDTO = getStudentRecursionDTO(studentEntity);
            this.students.add(studentRecursionDTO);
        }
    }

    private static GradeRecursionDTO toGradeRecursionDTO(GradeEntity gradeEntity) {
        GradeRecursionDTO gradeRecursionDTO = new GradeRecursionDTO();
        gradeRecursionDTO.setId(gradeEntity.getId());
        gradeRecursionDTO.setGrade(gradeEntity.getGrade());
        return gradeRecursionDTO;
    }

    private static StudentRecursionDTO getStudentRecursionDTO(StudentEntity studentEntity) {

        StudentRecursionDTO studentRecursionDTO = new StudentRecursionDTO();

        studentRecursionDTO.setId(studentEntity.getId());
        studentRecursionDTO.setName(studentEntity.getName());
        studentRecursionDTO.setLastName(studentEntity.getLastName());
        studentRecursionDTO.setEmail(studentEntity.getEmail());
        studentRecursionDTO.setDni(studentEntity.getDni());
        studentRecursionDTO.setNumSemester(studentEntity.getNumSemester());
        studentRecursionDTO.setAverage(studentEntity.getAverage());
        studentRecursionDTO.setCourses(getCourseSimpleRecursionDTO(studentEntity.getCourses(), studentEntity));

        return studentRecursionDTO;
    }


    private static Set<CourseSimpleRecursionDTO> getCourseSimpleRecursionDTO(Set<CourseEntity> courseEntities, StudentEntity studentEntity) {

        Set<CourseSimpleRecursionDTO> courseSimpleRecursionDTOS = new HashSet<>();

        for(CourseEntity courseEntity : courseEntities) {

            CourseSimpleRecursionDTO courseSimpleRecursionDTO = new CourseSimpleRecursionDTO();
            courseSimpleRecursionDTO.setId(courseEntity.getId());
            courseSimpleRecursionDTO.setName(courseEntity.getName());
            courseSimpleRecursionDTO.setAverage(courseEntity.getAverage());
            courseSimpleRecursionDTO.setGrades(StudentRecursionDTO.getGradesFromCourse(courseEntity.getGrades(), studentEntity, courseEntity));

            courseSimpleRecursionDTOS.add(courseSimpleRecursionDTO);
        }
        return courseSimpleRecursionDTOS;
    }
}
