package com.ubbackend.DTO;

public class StudentUpdateDTO {

    private String name;

    private String lastName;

    private String email;

    private Long dni;

    private int numSemester;

    private Float average;

    public Float getAverage() {
        return average;
    }

    public void setAverage(Float average) {
        this.average = average;
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
        return this.numSemester;
    }
    public void setNumSemester(int numSemester) {
        this.numSemester = numSemester;
    }
}