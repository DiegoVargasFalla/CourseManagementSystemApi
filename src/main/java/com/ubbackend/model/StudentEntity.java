package com.ubbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    private String email;

    @Column(nullable = false)
    private Long dni;

    @Column(nullable = false)
    private int numSemester = 1;

    @Column(nullable = false)
    private Float average = 0.0F;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "students")
    private Set<CourseEntity> courses = new HashSet<>();

    @OneToMany(mappedBy = "studentEntity", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonBackReference
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

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        StudentEntity that = (StudentEntity) obj;
        return Objects.equals(id, that.getDni());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.dni);
    }
}
