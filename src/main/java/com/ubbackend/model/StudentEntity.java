package com.ubbackend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Entity
@Table( name = "student")
public class StudentEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Long dni;

    @Column(nullable = false)
    private int numSemester = 1;

    @Column(nullable = false)
    private Float average = 0.0F;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "students")
    private Set<CourseEntity> courses = new HashSet<>();

    @OneToMany(mappedBy = "studentEntity")
    @JsonManagedReference
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

    public void setGrades(Set<GradeEntity> notesList) {
        this.grades = notesList;
    }

    public Set<CourseEntity> getCourses() {
        return courses;
    }

    public void setCourses(Set<CourseEntity> courseEntityList) {
        this.courses = courseEntityList;
    }
}
