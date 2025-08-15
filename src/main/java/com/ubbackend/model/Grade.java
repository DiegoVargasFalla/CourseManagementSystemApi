package com.ubbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "grade")
public class Grade {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private Float grade;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "studentId")
    @JsonBackReference
    private StudentEntity studentEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "courseId")
    @JsonBackReference
    private Course course;


    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Float getGrade() {
        return grade;
    }
    public void setGrade(Float grade) {
        this.grade = grade;
    }

    public StudentEntity getStudent() {
        return studentEntity;
    }
    public void setStudent(StudentEntity studentEntity) {
        this.studentEntity = studentEntity;
    }

    public Course getCourse() {
        return course;
    }
    public void setCourse(Course course) {
        this.course = course;
    }
}
