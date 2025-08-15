package com.ubbackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private float average;

    @ManyToMany(fetch = FetchType.EAGER, cascade ={ CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "course_student", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> listStudent;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "course")
    @JsonManagedReference
    private List<Grade> notes;



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

    public List<Student> getListStudent() {
        return listStudent;
    }
    public void setListStudent(List<Student> listStudent) {
        this.listStudent = listStudent;
    }

    public List<Grade> getNotes() {
        return notes;
    }
    public void setNotes(List<Grade> notes) {
        this.notes = notes;
    }
}
