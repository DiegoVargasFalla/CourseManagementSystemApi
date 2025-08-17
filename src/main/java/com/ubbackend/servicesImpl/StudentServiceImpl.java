package com.ubbackend.servicesImpl;

import com.ubbackend.DTOs.StudentDTO;
import com.ubbackend.DTOs.StudentUpdateDTO;
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

    @Override
    public Optional<StudentEntity> getStudent(Long dni) throws Exception {
        return studentRepository.findByDni(dni);
    }

    @Override
    public List<StudentEntity> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    public boolean deleteStudent(Long id) throws Exception {
        if(!studentRepository.existsById(id)){
            throw new Exception("Student not found!");
        }
        studentRepository.deleteById(id);
        return true;
    }

    @Override
    public Optional<StudentEntity> updateStudent(StudentUpdateDTO studentUpdateDTO) throws Exception{
        try{
            Optional<StudentEntity> student = studentRepository.findByDni(studentUpdateDTO.getDni());
            if(studentUpdateDTO.getName() != null){
                student.get().setName(studentUpdateDTO.getName());
            }
            if(studentUpdateDTO.getLastName() != null){
                student.get().setLastName(studentUpdateDTO.getLastName());
            }
            if(studentUpdateDTO.getNumSemester() == 1 || studentUpdateDTO.getNumSemester() == 2){
                student.get().setNumSemester(studentUpdateDTO.getNumSemester());
            }
            studentRepository.save(student.get());

            return Optional.of(student).get();
        }
        catch (Exception e) {
            throw new Exception("[You must include the student's dni] or [student not found]");
        }
    }
}
