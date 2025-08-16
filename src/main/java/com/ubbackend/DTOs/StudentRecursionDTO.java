package com.ubbackend.DTOs;

import com.ubbackend.model.CourseEntity;
import com.ubbackend.model.GradeEntity;
import com.ubbackend.model.StudentEntity;

import java.util.HashSet;
import java.util.Set;

public class StudentRecursionDTO {

    private Long id;

    private String name;

    private String lastName;

    private Long dni;

    private int numSemester = 1;

    private Float average = 0.0F;

    private Set<CourseSimpleRecursionDTO> courses = new HashSet<>();

    private Set<GradeEntity> grades = new HashSet<>();


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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public int getNumSemester() {
        return numSemester;
    }

    public void setNumSemester(int numSemester) {
        this.numSemester = numSemester;
    }

    public Float getAverage() {
        return average;
    }

    public void setAverage(Float average) {
        this.average = average;
    }

    public Set<GradeEntity> getGrades() {
        return grades;
    }

    public void setGrades(Set<GradeEntity> grades) {
        this.grades = grades;
    }

    public Set<CourseSimpleRecursionDTO> getCourses() {
        return courses;
    }

    public void setCourses(Set<CourseSimpleRecursionDTO> courses) {
        this.courses = courses;
    }

    public void toStudentRecursionDTO(StudentEntity studentEntity) {
        this.id = studentEntity.getId();
        this.name = studentEntity.getName();
        this.lastName = studentEntity.getLastName();
        this.dni = studentEntity.getDni();
        this.numSemester = studentEntity.getNumSemester();
        this.average = studentEntity.getAverage();
        this.grades = studentEntity.getGrades();

        for(CourseEntity courseEntity : studentEntity.getCourses()) {
            CourseSimpleRecursionDTO csrDTO = new CourseSimpleRecursionDTO();
            csrDTO.setId(courseEntity.getId());
            csrDTO.setName(courseEntity.getName());
            csrDTO.setAverage(courseEntity.getAverage());
            csrDTO.setGrades(courseEntity.getGrades());
            courses.add(csrDTO);
        }
    }

}
