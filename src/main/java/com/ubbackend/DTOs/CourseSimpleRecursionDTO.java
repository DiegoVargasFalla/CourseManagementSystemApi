package com.ubbackend.DTOs;

import com.ubbackend.model.GradeEntity;

import java.util.HashSet;
import java.util.Set;

public class CourseSimpleRecursionDTO {

    private Long id;

    private String name;

    private float average;

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

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public Set<GradeEntity> getGrades() {
        return grades;
    }

    public void setGrades(Set<GradeEntity> grades) {
        this.grades = grades;
    }
}
