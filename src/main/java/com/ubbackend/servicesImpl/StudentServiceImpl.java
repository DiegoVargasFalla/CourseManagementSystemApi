package com.ubbackend.servicesImpl;

import com.ubbackend.DTOs.StudentDTO;
import com.ubbackend.Exceptions.ResourceNotCreatedException;
import com.ubbackend.model.CourseEntity;
import com.ubbackend.model.StudentEntity;
import com.ubbackend.repository.CourseRepository;
import com.ubbackend.repository.StudentRepository;
import com.ubbackend.services.StudentService;

import java.util.List;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Optional<CourseEntity> createStudent(StudentDTO studentDTO) throws Exception{

        Optional<CourseEntity> courseExisting = courseRepository.findById(studentDTO.getIdCourse());

        if(courseExisting.isPresent()) {
            CourseEntity courseEntity = courseExisting.get();

            StudentEntity studentEntity = new StudentEntity();
            studentEntity.setName(studentDTO.getName());
            studentEntity.setLastName(studentDTO.getLastName());

            if(studentRepository.findByDni(studentDTO.getDni()).isEmpty()) {
                studentEntity.setDni(studentDTO.getDni());
            } else {
                throw new ResourceNotCreatedException("Dni is already used");
            }

            courseEntity.addStudent(studentEntity);
            return Optional.of(courseRepository.save(courseEntity));
        } else {
            throw new ResourceNotCreatedException("Course not exist");
        }
    }
}
