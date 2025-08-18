package com.ubbackend.DTO;

public class StudentUpdateDTO {

    private String name;

    private String lastName;

    private Long dni;

    private int numSemester;

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