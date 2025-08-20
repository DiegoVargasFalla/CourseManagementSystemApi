package com.ubbackend.servicesImpl;

import com.ubbackend.DTO.*;
import com.ubbackend.exception.NotFundCourseException;
import com.ubbackend.exception.ResourceNotCreatedException;
import com.ubbackend.enumeration.EShift;
import com.ubbackend.model.CourseEntity;
import com.ubbackend.model.StudentEntity;
import com.ubbackend.repository.CourseRepository;
import com.ubbackend.repository.StudentRepository;
import com.ubbackend.services.CourseService;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public CourseServiceImpl(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional
    public List<CourseRecursionDTO> getCourses() {

        List<CourseRecursionDTO> courseRecursionDTOList = new ArrayList<>();

        for(CourseEntity courseEntity : courseRepository.findAll()) {
            CourseRecursionDTO courseRecursionDTO = new CourseRecursionDTO();
            courseRecursionDTO.toCourseRecursionDTO(courseEntity);
            courseRecursionDTOList.add(courseRecursionDTO);
        }

        return courseRecursionDTOList;
    }

    @Override
    @Transactional
    public Optional<CourseRecursionDTO> getCourse(Long id) throws Exception {
        CourseRecursionDTO courseRecursionDTO = new CourseRecursionDTO();
        Optional<CourseEntity> courseExisting = courseRepository.findById(id);

        if(courseExisting.isEmpty()) {
            throw new NotFundCourseException("Course not exist");
        }
        courseRecursionDTO.toCourseRecursionDTO(courseExisting.get());
        return Optional.of(courseRecursionDTO);
    }

    @Override
    @Transactional
    public Optional<CourseEntity> createCourse(CourseDTO courseDTO) throws Exception {
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setName(courseDTO.getName());
        courseEntity.setAverage(0.0F);

        if(courseDTO.getShift().equals(EShift.MORNING.toString())) {
            courseEntity.setShift(EShift.MORNING);
        } else if(courseDTO.getShift().equals(EShift.AFTERNOON.toString())) {
            courseEntity.setShift(EShift.AFTERNOON);
        } else if(courseDTO.getShift().equals(EShift.EVENING.toString())) {
            courseEntity.setShift(EShift.EVENING);
        } else {
            throw new ResourceNotCreatedException("The course schedule is wrong");
        }

        try {
            return Optional.of(courseRepository.save(courseEntity));
        } catch (Exception e) {
            throw new ResourceNotCreatedException("Course couldn't be created", e.getCause());
        }
    }

    @Override
    public Optional<CourseEntity> updateCourse(CourseUpdateDTO courseUpdateDTO) throws Exception {
        Optional<CourseEntity> courseExisting = courseRepository.findById(courseUpdateDTO.getId());
        if(courseExisting.isPresent()) {
            CourseEntity courseEntity = courseExisting.get();
            if(courseUpdateDTO.getName() != null) {
                courseEntity.setName(courseUpdateDTO.getName());
            }
            if (courseUpdateDTO.getShift() != null) {
                courseEntity.setShift(courseUpdateDTO.getShift());
            }
            return Optional.of(courseRepository.save(courseEntity));
        } else {
            throw new NotFundCourseException("Course not found");
        }
    }

    @Override
    @Transactional
    public boolean deleteCourse(Long id) throws Exception {
        if(!courseRepository.existsById(id)) {
            return false;
        }
        courseRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public Optional<CourseRecursionDTO> newStudent(NewStudentDTO newStudentDTO) throws Exception {

        Optional<CourseEntity> courseExisting = courseRepository.findById(newStudentDTO.getCourseId());
        Optional<StudentEntity> studentExisting = studentRepository.findByDni(newStudentDTO.getDni());

        if( courseExisting.isPresent() && studentExisting.isPresent())  {

            StudentEntity studentEntity = studentExisting.get();
            CourseEntity courseEntity = courseExisting.get();

            if(!courseEntity.getStudents().contains(studentEntity)) {
                courseEntity.addStudent(studentEntity);
            } else {
                throw new ResourceNotCreatedException("Student already exists in this course");
            }

            CourseEntity createdCourseEntity = courseRepository.save(courseEntity);
            CourseRecursionDTO courseRecursionDTO = new CourseRecursionDTO();
            courseRecursionDTO.toCourseRecursionDTO(createdCourseEntity);
            return Optional.of(courseRecursionDTO);
        } else {
            throw new ResourceNotCreatedException("Could not created resource");
        }
    }

    @Override
    @Transactional
    public boolean deleteStudentFromCourse(NewStudentDTO studentDTO) {

        Optional<StudentEntity> studentExisting = studentRepository.findByDni(studentDTO.getDni());
        Optional<CourseEntity> courseExisting = courseRepository.findById(studentDTO.getCourseId());
        if(studentExisting.isPresent() && courseExisting.isPresent()) {
            StudentEntity studentEntity = studentExisting.get();
            CourseEntity courseEntity = courseExisting.get();

            courseEntity.getStudents().remove(studentEntity);
            courseRepository.save(courseEntity);
            return true;
        }
        throw new IllegalArgumentException("Course or student not found");
    }
}
