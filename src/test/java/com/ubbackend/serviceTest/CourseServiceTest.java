package com.ubbackend.serviceTest;

import com.ubbackend.DTO.CourseDTO;
import com.ubbackend.DTO.CourseRecursionDTO;
import com.ubbackend.DTO.CourseUpdateDTO;
import com.ubbackend.DTO.NewStudentDTO;
import com.ubbackend.enumeration.EShift;
import com.ubbackend.exception.NotFundCourseException;
import com.ubbackend.exception.ResourceNotCreatedException;
import com.ubbackend.model.CourseEntity;
import com.ubbackend.model.StudentEntity;
import com.ubbackend.repository.CourseRepository;
import com.ubbackend.repository.StudentRepository;
import com.ubbackend.servicesImpl.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CourseServiceTest {


    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ------------------------------------------------------------------
    // getCourses()
    // ------------------------------------------------------------------
    @Test
    void testGetCourses_ReturnsList() {
        CourseEntity course1 = new CourseEntity();
        course1.setId(1L);
        course1.setName("Matemática");

        CourseEntity course2 = new CourseEntity();
        course2.setId(2L);
        course2.setName("Física");

        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        List<CourseRecursionDTO> result = courseService.getCourses();

        assertEquals(2, result.size());
        verify(courseRepository, times(1)).findAll();
    }

    // ------------------------------------------------------------------
    // getCourse()
    // ------------------------------------------------------------------
    @Test
    void testGetCourse_Found() {
        CourseEntity course = new CourseEntity();
        course.setId(10L);
        course.setName("Programación");

        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));

        Optional<CourseRecursionDTO> result = courseService.getCourse(10L);

        assertTrue(result.isPresent());
        verify(courseRepository, times(1)).findById(10L);
    }

    @Test
    void testGetCourse_NotFound_ReturnsEmpty() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<CourseRecursionDTO> result = courseService.getCourse(99L);

        assertTrue(result.isEmpty());
        verify(courseRepository, times(1)).findById(99L);
    }

    // ------------------------------------------------------------------
    // createCourse()
    // ------------------------------------------------------------------
    @Test
    void testCreateCourse_Success() throws Exception {
        CourseDTO dto = new CourseDTO();
        dto.setName("Química");
        dto.setShift(EShift.MORNING.toString());

        CourseEntity saved = new CourseEntity();
        saved.setId(1L);
        saved.setName("Química");
        saved.setShift(EShift.MORNING);

        when(courseRepository.save(any(CourseEntity.class))).thenReturn(saved);

        Optional<CourseEntity> result = courseService.createCourse(dto);

        assertTrue(result.isPresent());
        assertEquals("Química", result.get().getName());
        verify(courseRepository, times(1)).save(any(CourseEntity.class));
    }

    @Test
    void testCreateCourse_InvalidShift_ReturnsEmpty() throws Exception {
        CourseDTO dto = new CourseDTO();
        dto.setName("Historia");
        dto.setShift("INVALIDO");

        Optional<CourseEntity> result = courseService.createCourse(dto);

        assertTrue(result.isEmpty());
        verify(courseRepository, never()).save(any());
    }

    // ------------------------------------------------------------------
    // updateCourse()
    // ------------------------------------------------------------------
    @Test
    void testUpdateCourse_Success() throws Exception {
        CourseEntity existing = new CourseEntity();
        existing.setId(2L);
        existing.setName("Matemática");
        existing.setShift(EShift.MORNING);

        CourseUpdateDTO updateDTO = new CourseUpdateDTO();
        updateDTO.setId(2L);
        updateDTO.setName("Matemática Avanzada");
        updateDTO.setShift(EShift.AFTERNOON);

        when(courseRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(courseRepository.save(any(CourseEntity.class))).thenAnswer(i -> i.getArgument(0));

        Optional<CourseEntity> result = courseService.updateCourse(updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Matemática Avanzada", result.get().getName());
        assertEquals(EShift.AFTERNOON, result.get().getShift());
        verify(courseRepository, times(1)).save(existing);
    }

    @Test
    void testUpdateCourse_NotFound_ThrowsException() {
        CourseUpdateDTO updateDTO = new CourseUpdateDTO();
        updateDTO.setId(999L);
        updateDTO.setName("NoExiste");

        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFundCourseException.class, () -> courseService.updateCourse(updateDTO));
    }

    // ------------------------------------------------------------------
    // deleteCourse()
    // ------------------------------------------------------------------
    @Test
    void testDeleteCourse_Success() throws Exception {
        when(courseRepository.existsById(1L)).thenReturn(true);

        boolean result = courseService.deleteCourse(1L);

        assertTrue(result);
        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCourse_NotFound_ReturnsFalse() throws Exception {
        when(courseRepository.existsById(5L)).thenReturn(false);

        boolean result = courseService.deleteCourse(5L);

        assertFalse(result);
        verify(courseRepository, never()).deleteById(anyLong());
    }

    // ------------------------------------------------------------------
    // newStudent()
    // ------------------------------------------------------------------
    @Test
    void testNewStudent_Success() throws Exception {
        StudentEntity student = new StudentEntity();
        student.setDni(123L);

        CourseEntity course = new CourseEntity();
        course.setId(1L);
        course.setStudents(new HashSet<>());

        NewStudentDTO dto = new NewStudentDTO();
        dto.setCourseId(1L);
        dto.setDni(123L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.findByDni(123L)).thenReturn(Optional.of(student));
        when(courseRepository.save(any(CourseEntity.class))).thenAnswer(i -> i.getArgument(0));

        Optional<CourseRecursionDTO> result = courseService.newStudent(dto);

        assertTrue(result.isPresent());
        assertEquals(1, course.getStudents().size());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testNewStudent_AlreadyExists_ThrowsException() {
        StudentEntity student = new StudentEntity();
        student.setDni(123L);

        CourseEntity course = new CourseEntity();
        course.setId(1L);
        course.setStudents(new HashSet<>(Set.of(student)));

        NewStudentDTO dto = new NewStudentDTO();
        dto.setCourseId(1L);
        dto.setDni(123L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.findByDni(123L)).thenReturn(Optional.of(student));

        assertThrows(ResourceNotCreatedException.class, () -> courseService.newStudent(dto));
        verify(courseRepository, never()).save(any());
    }

    @Test
    void testNewStudent_NotFound_ReturnsEmpty() throws Exception {
        NewStudentDTO dto = new NewStudentDTO();
        dto.setCourseId(10L);
        dto.setDni(999L);

        when(courseRepository.findById(10L)).thenReturn(Optional.empty());
        when(studentRepository.findByDni(999L)).thenReturn(Optional.empty());

        Optional<CourseRecursionDTO> result = courseService.newStudent(dto);

        assertTrue(result.isEmpty());
        verify(courseRepository, never()).save(any());
    }

    // ------------------------------------------------------------------
    // deleteStudentFromCourse()
    // ------------------------------------------------------------------
    @Test
    void testDeleteStudentFromCourse_Success() {
        StudentEntity student = new StudentEntity();
        student.setDni(111L);

        CourseEntity course = new CourseEntity();
        course.setId(1L);
        course.setStudents(new HashSet<>(Set.of(student)));

        NewStudentDTO dto = new NewStudentDTO();
        dto.setCourseId(1L);
        dto.setDni(111L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(CourseEntity.class))).thenAnswer(i -> i.getArgument(0));

        Optional<CourseRecursionDTO> result = courseService.deleteStudentFromCourse(dto);

        assertTrue(result.isPresent());
        assertTrue(course.getStudents().isEmpty());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testDeleteStudentFromCourse_NotFound_ReturnsEmpty() {
        NewStudentDTO dto = new NewStudentDTO();
        dto.setCourseId(1L);
        dto.setDni(999L);

        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<CourseRecursionDTO> result = courseService.deleteStudentFromCourse(dto);

        assertTrue(result.isEmpty());
        verify(courseRepository, never()).save(any());
    }
}
