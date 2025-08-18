package com.ubbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name = "grade")
public class GradeEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private Float grade;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "studentId")
    @JsonManagedReference
    private StudentEntity studentEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "courseId")
    @JsonManagedReference
    private CourseEntity courseEntity;

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

    public CourseEntity getCourse() {
        return courseEntity;
    }

    public void setCourse(CourseEntity courseEntity) {
        this.courseEntity = courseEntity;
    }

    public StudentEntity getStudentEntity() {
        return studentEntity;
    }

    public void setStudentEntity(StudentEntity studentEntity) {
        this.studentEntity = studentEntity;
    }

    public CourseEntity getCourseEntity() {
        return courseEntity;
    }

    public void setCourseEntity(CourseEntity courseEntity) {
        this.courseEntity = courseEntity;
    }
}
