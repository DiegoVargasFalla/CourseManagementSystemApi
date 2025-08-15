package com.ubbackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table( name = "student")
public class Student {

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

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "listStudent")
    private List<Course> courseList;

    @OneToMany(mappedBy = "student")
    @JsonManagedReference
    private List<Grade> notesList;



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

    public List<Course> getCourseList() {
        return courseList;
    }
    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public List<Grade> getNotesList() {
        return notesList;
    }
    public void setNotesList(List<Grade> notesList) {
        this.notesList = notesList;
    }
}
