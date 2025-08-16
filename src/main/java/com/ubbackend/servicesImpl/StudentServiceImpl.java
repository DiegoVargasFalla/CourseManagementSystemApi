package com.ubbackend.servicesImpl;

import com.ubbackend.DTOs.CourseRecursionDTO;
import com.ubbackend.DTOs.StudentDTO;
import com.ubbackend.DTOs.StudentRecursionDTO;
import com.ubbackend.Exceptions.ResourceNotCreatedException;
import com.ubbackend.model.CourseEntity;
import com.ubbackend.model.StudentEntity;
import com.ubbackend.repository.CourseRepository;
import com.ubbackend.repository.StudentRepository;
import com.ubbackend.services.StudentService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public List<StudentRecursionDTO> getStudents() {

        List<StudentRecursionDTO> studentRecursionDTOList = new ArrayList<>();

        for(StudentEntity student : studentRepository.findAll()) {
            StudentRecursionDTO studentRecursionDTO = new StudentRecursionDTO();
            studentRecursionDTO.toStudentRecursionDTO(student);
            studentRecursionDTOList.add(studentRecursionDTO);
        }
        return studentRecursionDTOList;
    }

    @Override
    public Optional<CourseRecursionDTO> createStudent(StudentDTO studentDTO) throws Exception{

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
            CourseEntity newCourseEntity = courseRepository.save(courseEntity);
            CourseRecursionDTO courseRecursionDTO = new CourseRecursionDTO();
            courseRecursionDTO.toCourseRecursionDTO(newCourseEntity);
            return Optional.of(courseRecursionDTO);
        } else {
            throw new ResourceNotCreatedException("Course not exist");
        }
    }
}
