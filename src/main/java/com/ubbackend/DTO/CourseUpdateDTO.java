package com.ubbackend.DTO;

import com.ubbackend.enumeration.EShift;

public class CourseUpdateDTO {
    private Long id;
    private String name;
    private EShift shift;

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

    public EShift getShift() {
        return shift;
    }

    public void setShift(EShift shift) {
        this.shift = shift;
    }
}
