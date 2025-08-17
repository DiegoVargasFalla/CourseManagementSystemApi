package com.ubbackend.DTOs;

public class StudentUpdateDTO {
    private Long id;
    private String name;

    private String lastName;

    private Long dni;

    private int numSemester;

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
        return this.numSemester;
    }
    public void setNumSemester(int numSemester) {
        this.numSemester = numSemester;
    }
}