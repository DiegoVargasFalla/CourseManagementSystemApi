package com.ubbackend.DTO;

import com.ubbackend.model.CourseEntity;
import com.ubbackend.model.GradeEntity;
import com.ubbackend.model.StudentEntity;

import java.util.HashSet;
import java.util.Set;

public class StudentRecursionDTO {

    private Long id;

    private String name;

    private String lastName;

    private String email;

    private Long dni;

    private int numSemester = 1;

    private Float average = 0.0F;

    private Set<CourseSimpleRecursionDTO> courses = new HashSet<>();


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Set<CourseSimpleRecursionDTO> getCourses() {
        return courses;
    }

    public void setCourses(Set<CourseSimpleRecursionDTO> courses) {
        this.courses = courses;
    }

    public void toStudentRecursionDTO(StudentEntity studentEntity) {
        this.id = studentEntity.getId();
        this.name = studentEntity.getName();
        this.lastName = studentEntity.getLastName();
        this.dni = studentEntity.getDni();
        this.numSemester = studentEntity.getNumSemester();
        this.average = studentEntity.getAverage();

        for(CourseEntity courseEntity : studentEntity.getCourses()) {
            CourseSimpleRecursionDTO csrDTO = new CourseSimpleRecursionDTO();
            csrDTO.setId(courseEntity.getId());
            csrDTO.setName(courseEntity.getName());
            csrDTO.setAverage(courseEntity.getAverage());
            csrDTO.setGrades(getGradesFromCourse(courseEntity.getGrades(), studentEntity, courseEntity));
            courses.add(csrDTO);
        }
    }

    public static Set<GradeRecursionDTO> getGradesFromCourse(Set<GradeEntity> gradeEntities, StudentEntity studentEntity, CourseEntity courseEntity) {
        Set<GradeRecursionDTO> gradesRecursionDTO = new HashSet<>();
        for(GradeEntity gradeEntity: gradeEntities) {
            if(gradeEntity.getStudent().getId().equals(studentEntity.getId()) && gradeEntity.getCourse().getId().equals(courseEntity.getId())) {
                GradeRecursionDTO gradeRecursionDTO = new GradeRecursionDTO();
                gradeRecursionDTO.setId(gradeEntity.getId());
                gradeRecursionDTO.setGrade(gradeEntity.getGrade());
                gradesRecursionDTO.add(gradeRecursionDTO);
            }
        }
        return gradesRecursionDTO;
    }

}
