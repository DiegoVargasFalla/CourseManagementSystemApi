package com.ubbackend.servicesImpl;

import com.ubbackend.DTO.*;
import com.ubbackend.exception.ResourceNotCreatedException;
import com.ubbackend.exception.UserExistException;
import com.ubbackend.model.CourseEntity;
import com.ubbackend.model.GradeEntity;
import com.ubbackend.model.StudentEntity;
import com.ubbackend.repository.CourseRepository;
import com.ubbackend.repository.GradeRepository;
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
    private final GradeRepository gradeRepository;

    public StudentServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository, GradeRepository gradeRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.gradeRepository = gradeRepository;
    }

    @Override
    @Transactional
    public List<StudentRecursionDTO> getStudents() {

        List<StudentRecursionDTO> studentRecursionDTOList = new ArrayList<>();

        for(StudentEntity student: studentRepository.findAll()) {
            StudentRecursionDTO studentRecursionDTO = new StudentRecursionDTO();

            studentRecursionDTO.toStudentRecursionDTO(student);
            studentRecursionDTOList.add(studentRecursionDTO);
        }
        return studentRecursionDTOList;
    }

    @Override
    public Optional<StudentRecursionDTO> getStudent(Long id) throws Exception {
        Optional<StudentEntity> studentExisting = studentRepository.findById(id);
        if(studentExisting.isEmpty()) {
            throw new UserExistException("Student not found");
        }
        StudentRecursionDTO studentRecursionDTO = new StudentRecursionDTO();
        studentRecursionDTO.toStudentRecursionDTO(studentExisting.get());
        return Optional.of(studentRecursionDTO);
    }

    @Override
    @Transactional
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

    @Override
    public boolean deleteStudent(Long id) throws Exception {
        if(!studentRepository.existsById(id)){
            throw new Exception("Student not found!");
        }
        studentRepository.deleteById(id);
        return true;
    }

    @Override
    public Optional<StudentRecursionDTO> updateStudent(Long id, StudentUpdateDTO studentUpdateDTO) throws Exception{
        Optional<StudentEntity> studentExisting = studentRepository.findById(id);
        
        if(studentExisting.isPresent()){
            StudentEntity student = studentExisting.get();

            if(studentUpdateDTO.getName() != null){
                student.setName(studentUpdateDTO.getName());
            }
            if(studentUpdateDTO.getLastName() != null){
                student.setLastName(studentUpdateDTO.getLastName());
            }
            if(studentUpdateDTO.getNumSemester() != 0){
                student.setNumSemester(studentUpdateDTO.getNumSemester());
            }
            if(studentUpdateDTO.getDni() != 0){
                student.setDni(studentUpdateDTO.getDni());
            }

            StudentEntity studentEntity = studentRepository.save(student);
            StudentRecursionDTO studentRecursionDTO = new StudentRecursionDTO();
            studentRecursionDTO.toStudentRecursionDTO(studentEntity);

            return Optional.of(studentRecursionDTO);
        }
        return Optional.empty();
    }

    @Override
    public Optional<StudentRecursionDTO> addGradeToStudent(StudentGradeDTO studentGradeDTO) throws Exception {
        Optional<StudentEntity> studentExisting = studentRepository.findById(studentGradeDTO.getStudentId());
        Optional<CourseEntity> courseExisting = courseRepository.findById(studentGradeDTO.getCourseId());

        if(studentExisting.isPresent() && courseExisting.isPresent()) {

            StudentEntity studentEntity = studentExisting.get();
            CourseEntity courseEntity = courseExisting.get();
            GradeEntity gradeEntity = new GradeEntity();

            gradeEntity.setGrade(studentGradeDTO.getGrade());
            gradeEntity.setStudent(studentEntity);
            gradeEntity.setCourse(courseEntity);

            gradeRepository.save(gradeEntity);

            StudentRecursionDTO studentRecursionDTO = new StudentRecursionDTO();
            studentRecursionDTO.toStudentRecursionDTO(studentEntity);
            return Optional.of(studentRecursionDTO);

        }
        return Optional.empty();
    }
}
