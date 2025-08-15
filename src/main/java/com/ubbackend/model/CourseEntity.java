package com.ubbackend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ubbackend.enumerations.EShift;
import jakarta.persistence.*;

import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
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
    private List<StudentEntity> students;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, orphanRemoval = true, mappedBy = "courseEntity")
    @JsonManagedReference
    private List<GradeEntity> grades;

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

    public List<StudentEntity> getStudents() {
        return students;
    }

    public void setStudents(List<StudentEntity> listStudentEntity) {
        this.students = listStudentEntity;
    }

    public void setListStudent(List<StudentEntity> listStudentEntity) {
        this.students = listStudentEntity;
    }

    public List<GradeEntity> getGrades() {
        return grades;
    }

    public void setGrades(List<GradeEntity> notes) {
        this.grades = notes;
    }
}
