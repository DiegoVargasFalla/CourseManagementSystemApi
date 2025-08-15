package com.ubbackend.servicesImpl;

import com.ubbackend.model.Student;
import com.ubbackend.repository.StudentRepository;
import com.ubbackend.services.StudentService;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    @Override
    public String proof() {
        return "This is a proof";
    }

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }
}
