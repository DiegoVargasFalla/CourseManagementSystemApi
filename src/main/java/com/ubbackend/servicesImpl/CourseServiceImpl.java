package com.ubbackend.servicesImpl;

import com.ubbackend.DTOs.CourseDTO;
import com.ubbackend.DTOs.CourseUpdateDTO;
import com.ubbackend.DTOs.NewStudentDTO;
import com.ubbackend.Exceptions.NotFundCourseException;
import com.ubbackend.Exceptions.ResourceNotCreatedException;
import com.ubbackend.enumerations.EShift;
import com.ubbackend.model.CourseEntity;
import com.ubbackend.model.StudentEntity;
import com.ubbackend.repository.CourseRepository;
import com.ubbackend.repository.StudentRepository;
import com.ubbackend.services.CourseService;
import org.springframework.stereotype.Service;

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
    public List<CourseEntity> getCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Optional<CourseEntity> getCourse(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public boolean createCourse(CourseDTO courseDTO) throws Exception {
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setName(courseDTO.getName());
        courseEntity.setAverage(0.0F);

        if(courseDTO.getShift().equals(EShift.MORNING.toString())) {
            courseEntity.setShift(EShift.MORNING);
        } else if(courseDTO.getShift().equals(EShift.AFTERNOON.toString())) {
            courseEntity.setShift(EShift.MORNING);
        } else if(courseDTO.getShift().equals(EShift.EVENING.toString())) {
            courseEntity.setShift(EShift.MORNING);
        } else {
            throw new ResourceNotCreatedException("The course schedule is wrong");
        }

        try {
            courseRepository.save(courseEntity);
        } catch (Exception e) {
            throw new ResourceNotCreatedException("Course couldn't be created");
        }
        return true;
    }

    @Override
    public void updateCourse(CourseUpdateDTO courseUpdateDTO) throws Exception{
        Optional<CourseEntity> courseExisting = courseRepository.findById(courseUpdateDTO.getId());
        if(courseExisting.isPresent()) {
            CourseEntity courseEntity = courseExisting.get();
            if(courseUpdateDTO.getName() != null) {
                courseEntity.setName(courseUpdateDTO.getName());
            }
            courseRepository.save(courseEntity);
        } else {
            throw new NotFundCourseException("Course not found");
        }
    }

    @Override
    public void deleteCourse(Long id) throws Exception {
        if(!courseRepository.existsById(id)) {
            throw new NotFundCourseException("Course not found");
        }
        courseRepository.deleteById(id);
    }

    @Override
    public Optional<CourseEntity> newStudent(NewStudentDTO newStudentDTO) throws Exception {

        Optional<CourseEntity> courseExisting = courseRepository.findById(newStudentDTO.getCourseId());
        Optional<StudentEntity> studentExisting = studentRepository.findByDni(newStudentDTO.getDni());

        if( courseExisting.isPresent() && studentExisting.isPresent())  {

            StudentEntity studentEntity = studentExisting.get();
            CourseEntity courseEntity = courseExisting.get();
            courseEntity.addStudent(studentEntity);

            return Optional.of(courseRepository.save(courseEntity));
        } else {
            throw new ResourceNotCreatedException("Could not created resource");
        }
    }
}
