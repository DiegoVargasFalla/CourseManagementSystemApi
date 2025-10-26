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

    /**
     * metodo para obtener la lista de estudiantes
     * @return Lista con StudentRecursionDTO
     */
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

    /**
     * metodo para buscar un eestudiante
     * @param id del usuario a buscar
     * @return un Optional con un StudentRecursionDTO dentro.
     * @throws Exception si no exite el estudante informa con una excepcion
     */
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

    /**
     * metodo para crear un estudiante
     * @param studentDTO clase con los atributos del estudiante
     * @return Optional con el curso donde se creo el estudiante
     * @throws Exception si el estudiante ya existe o el curso no existe
     */
    @Override
    @Transactional
    public Optional<CourseRecursionDTO> createStudent(StudentDTO studentDTO) throws Exception{

        Optional<CourseEntity> courseExisting = courseRepository.findById(studentDTO.getIdCourse());

        if(courseExisting.isPresent()) {
            CourseEntity courseEntity = courseExisting.get();

            StudentEntity studentEntity = new StudentEntity();
            studentEntity.setName(studentDTO.getName());
            studentEntity.setLastName(studentDTO.getLastName());
            studentEntity.setEmail(studentDTO.getEmail());

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
            return Optional.empty();
        }
    }

    /**
     * metodo para actualizar un estudiante
     * @param id atributo para buscar el estudiante
     * @param studentUpdateDTO datos del estudiante a actualizar
     * @return Optional con StudentRecursionDTO
     */
    @Override
    public Optional<StudentRecursionDTO> updateStudent(Long id, StudentUpdateDTO studentUpdateDTO) {
        Optional<StudentEntity> studentExisting = studentRepository.findById(id);
        
        if(studentExisting.isPresent()){
            StudentEntity student = studentExisting.get();

            if(studentUpdateDTO.getName() != null){
                student.setName(studentUpdateDTO.getName());
            }
            if(studentUpdateDTO.getLastName() != null){
                student.setLastName(studentUpdateDTO.getLastName());
            }
            if(studentUpdateDTO.getEmail() != null){
                student.setLastName(studentUpdateDTO.getLastName());
            }
            if(studentUpdateDTO.getNumSemester() != 0){
                student.setNumSemester(studentUpdateDTO.getNumSemester());
            }
            if(studentUpdateDTO.getDni() != 0){
                student.setDni(studentUpdateDTO.getDni());
            } if(studentUpdateDTO.getAverage() != 0) {
                student.setAverage(studentUpdateDTO.getAverage());
            }

            StudentEntity studentEntity = studentRepository.save(student);
            StudentRecursionDTO studentRecursionDTO = new StudentRecursionDTO();
            studentRecursionDTO.toStudentRecursionDTO(studentEntity);

            return Optional.of(studentRecursionDTO);
        }
        return Optional.empty();
    }

    /**
     * metodo para agregar una nota al estudiante
     * @param studentGradeDTO datos para crear la nota
     * @return Optional con StudentRecursionDTO si se agrego la nota y si no empty (sin nada)
     */
    @Override
    public Optional<StudentRecursionDTO> addGradeToStudent(StudentGradeDTO studentGradeDTO) {
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
