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

    /**
     * metodo para obtener todos los cursos existentes
     * @return lista con todos los cursos
     */
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

    /**
     * metodo para obtener un curso especifico
     * @param id del curso que se quiere obtener
     * @return Optional con el curso que se solicito con toda la informacion completa del mismo
     */
    @Override
    @Transactional
    public Optional<CourseRecursionDTO> getCourse(Long id) {
        CourseRecursionDTO courseRecursionDTO = new CourseRecursionDTO();
        Optional<CourseEntity> courseExisting = courseRepository.findById(id);

        if(courseExisting.isEmpty()) {
            return Optional.empty();
        }
        courseRecursionDTO.toCourseRecursionDTO(courseExisting.get());
        return Optional.of(courseRecursionDTO);
    }

    /**
     * metodo para crear un curso
     * @param courseDTO esta clase debe tener los atributos necesarios para crear el curso
     * @return Optional con el curso creado
     * @throws Exception en caso de que el turno del curso este mal escrito lanza una excepcion
     * informando que el turno esta mal escrito
     */
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
            return Optional.empty();
        }
        return Optional.of(courseRepository.save(courseEntity));
    }

    /**
     * metodo para actualizar un curso
     * @param courseUpdateDTO atributos del curso a actualizar
     * @return Optional con el mismo curso que se acaba de actualizar
     * @throws Exception en caso de que no se encuntre el curso con el id enviado, lanzara una excepcion
     */
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

    /**
     * metodo para eliminar un curso
     * @param id del curso a eliminar
     * @return valor booleano (true) si se elmino el curso, (false) si el curso que se quiere eliminar no existe
     */
    @Override
    @Transactional
    public boolean deleteCourse(Long id) throws Exception {
        if(!courseRepository.existsById(id)) {
            return false;
        }
        courseRepository.deleteById(id);
        return true;
    }

    /**
     * metodo para agregar un estudiante a un curso.
     * @param newStudentDTO clase con los atributos necesarios para agregar un estudiante a un curso especifico
     * @return Optional DTO del curso al cual se le agrego un estudiante
     * @throws Exception en caso de que el estudiante ya exista en el curso, lanzara una excepcion;
     * la verificacion de si ya existe, se hace por medio del dni.
     * en caso de que el estudiante que se quiere agragar no exista o el curso no exista,
     * lanzara un error infomrando que el curso o el estudiante no existe.
     */
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
            return Optional.empty();
        }
    }

    /**
     * metodo para eliminar un estudiante de un curso
     * @param studentDTO atributos neceasarios para elminar el estudiante
     * @return valor boooleano (true) si se elimino el estudiante, si el
     * estudiante no existe o el curso del que se quiere eliminar el
     * estudiante no existe lanza una excepcion
     */
    @Override
    @Transactional
    public Optional<CourseRecursionDTO> deleteStudentFromCourse(NewStudentDTO studentDTO) {

        Optional<StudentEntity> studentExisting = studentRepository.findByDni(studentDTO.getDni());
        Optional<CourseEntity> courseExisting = courseRepository.findById(studentDTO.getCourseId());

        if(studentExisting.isPresent() && courseExisting.isPresent()) {
            StudentEntity studentEntity = studentExisting.get();
            CourseEntity courseEntity = courseExisting.get();

            courseEntity.getStudents().remove(studentEntity);
            CourseEntity courseEntityUpdate =  courseRepository.save(courseEntity);

            CourseRecursionDTO courseRecursionDTO = new CourseRecursionDTO();
            courseRecursionDTO.toCourseRecursionDTO(courseEntityUpdate);

            return Optional.of(courseRecursionDTO);
        }
        return Optional.empty();
    }
}
