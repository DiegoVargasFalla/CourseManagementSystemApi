package com.ubbackend.services;

import java.util.List;

import com.ubbackend.model.Student;

public interface StudentService {
    String proof();
    List<Student> getStudents();
}
