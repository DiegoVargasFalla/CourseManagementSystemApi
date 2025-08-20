package com.ubbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ubbackend.enumeration.EShift;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "course")
public class CourseEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private float average;

    @Enumerated(EnumType.STRING)
    private EShift shift;

    @ManyToMany(fetch = FetchType.LAZY, cascade ={ CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "course_student", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Set<StudentEntity> students = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true, mappedBy = "courseEntity")
    @JsonBackReference
    private Set<GradeEntity> grades = new HashSet<>();

    public void addStudent(StudentEntity studentEntity) {
        students.add(studentEntity);
        studentEntity.getCourses().add(this);
    }

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

    public EShift getShift() {
        return shift;
    }

    public void setShift(EShift turn) {
        this.shift = turn;
    }

    public Set<StudentEntity> getStudents() {
        return students;
    }

    public void setStudents(Set<StudentEntity> listStudentEntity) {
        this.students = listStudentEntity;
    }

    public void setListStudent(Set<StudentEntity> listStudentEntity) {
        this.students = listStudentEntity;
    }

    public Set<GradeEntity> getGrades() {
        return grades;
    }

    public void setGrades(Set<GradeEntity> notes) {
        this.grades = notes;
    }
}
