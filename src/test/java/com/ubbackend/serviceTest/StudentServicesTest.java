package com.ubbackend.serviceTest;

import com.ubbackend.DTO.*;
import com.ubbackend.exception.ResourceNotCreatedException;
import com.ubbackend.exception.UserExistException;
import com.ubbackend.model.CourseEntity;
import com.ubbackend.model.GradeEntity;
import com.ubbackend.model.StudentEntity;
import com.ubbackend.repository.CourseRepository;
import com.ubbackend.repository.GradeRepository;
import com.ubbackend.repository.StudentRepository;
import com.ubbackend.servicesImpl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StudentServicesTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private GradeRepository gradeRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ------------------------------------------------------------------
    // getStudents()
    // ------------------------------------------------------------------
    @Test
    void testGetStudents_ReturnsList() {
        StudentEntity student1 = new StudentEntity();
        student1.setId(1L);
        student1.setName("Andrés");
        student1.setLastName("Fall");
        student1.setEmail("andres@example.com");

        StudentEntity student2 = new StudentEntity();
        student2.setId(2L);
        student2.setName("María");
        student2.setLastName("Gómez");
        student2.setEmail("maria@example.com");

        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));

        List<StudentRecursionDTO> result = studentService.getStudents();

        assertEquals(2, result.size());
        assertEquals("Andrés", result.get(0).getName());
        assertEquals("María", result.get(1).getName());
        verify(studentRepository, times(1)).findAll();
    }

    // ------------------------------------------------------------------
    // getStudent()
    // ------------------------------------------------------------------
    @Test
    void testGetStudent_StudentExists() throws Exception {
        StudentEntity student = new StudentEntity();
        student.setId(1L);
        student.setName("Carlos");
        student.setEmail("carlos@example.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Optional<StudentRecursionDTO> result = studentService.getStudent(1L);

        assertTrue(result.isPresent());
        assertEquals("Carlos", result.get().getName());
        assertEquals("carlos@example.com", result.get().getEmail());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetStudent_NotFound_ThrowsException() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserExistException.class, () -> studentService.getStudent(1L));
        verify(studentRepository, times(1)).findById(1L);
    }

    // ------------------------------------------------------------------
    // createStudent()
    // ------------------------------------------------------------------
    @Test
    void testCreateStudent_Success() throws Exception {
        CourseEntity course = new CourseEntity();
        course.setId(10L);
        course.setName("Matemática");

        StudentDTO dto = new StudentDTO();
        dto.setIdCourse(10L);
        dto.setName("Pedro");
        dto.setLastName("Pérez");
        dto.setEmail("pedro@example.com");
        dto.setDni(123456L);

        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
        when(studentRepository.findByDni(123456L)).thenReturn(Optional.empty());
        when(courseRepository.save(any(CourseEntity.class))).thenAnswer(i -> i.getArgument(0));

        Optional<CourseRecursionDTO> result = studentService.createStudent(dto);

        assertTrue(result.isPresent());
        verify(courseRepository, times(1)).findById(10L);
        verify(studentRepository, times(1)).findByDni(123456L);
        verify(courseRepository, times(1)).save(any(CourseEntity.class));
    }

    @Test
    void testCreateStudent_DniAlreadyUsed_ThrowsException() {
        CourseEntity course = new CourseEntity();
        course.setId(10L);

        StudentDTO dto = new StudentDTO();
        dto.setIdCourse(10L);
        dto.setName("Juan");
        dto.setDni(123456L);

        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
        when(studentRepository.findByDni(123456L)).thenReturn(Optional.of(new StudentEntity()));

        assertThrows(ResourceNotCreatedException.class, () -> studentService.createStudent(dto));
        verify(studentRepository, times(1)).findByDni(123456L);
    }

    @Test
    void testCreateStudent_CourseNotFound_ReturnsEmpty() throws Exception {
        StudentDTO dto = new StudentDTO();
        dto.setIdCourse(99L);
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<CourseRecursionDTO> result = studentService.createStudent(dto);

        assertTrue(result.isEmpty());
        verify(courseRepository, times(1)).findById(99L);
    }

    // ------------------------------------------------------------------
    // updateStudent()
    // ------------------------------------------------------------------
    @Test
    void testUpdateStudent_Success() {
        StudentEntity existing = new StudentEntity();
        existing.setId(1L);
        existing.setName("OldName");
        existing.setEmail("old@example.com");

        StudentUpdateDTO updateDTO = new StudentUpdateDTO();
        updateDTO.setName("NewName");
        updateDTO.setEmail("new@example.com");
        updateDTO.setDni(999L);
        updateDTO.setAverage(0F);
        updateDTO.setNumSemester(0);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentRepository.save(any(StudentEntity.class))).thenAnswer(i -> i.getArgument(0));

        Optional<StudentRecursionDTO> result = studentService.updateStudent(1L, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("NewName", result.get().getName());
        assertEquals("new@example.com", result.get().getEmail());
        verify(studentRepository, times(1)).save(existing);
    }

    @Test
    void testUpdateStudent_NotFound_ReturnsEmpty() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<StudentRecursionDTO> result = studentService.updateStudent(1L, new StudentUpdateDTO());
        assertTrue(result.isEmpty());
    }

    // ------------------------------------------------------------------
    // addGradeToStudent()
    // ------------------------------------------------------------------
    @Test
    void testAddGradeToStudent_Success() {
        StudentEntity student = new StudentEntity();
        student.setId(1L);
        student.setName("Mario");

        CourseEntity course = new CourseEntity();
        course.setId(2L);
        course.setName("Física");

        StudentGradeDTO dto = new StudentGradeDTO();
        dto.setStudentId(1L);
        dto.setCourseId(2L);
        dto.setGrade(8.5F);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));
        when(gradeRepository.save(any(GradeEntity.class))).thenAnswer(i -> i.getArgument(0));

        Optional<StudentRecursionDTO> result = studentService.addGradeToStudent(dto);

        assertTrue(result.isPresent());
        verify(gradeRepository, times(1)).save(any(GradeEntity.class));
    }

    @Test
    void testAddGradeToStudent_NotFound_ReturnsEmpty() {
        StudentGradeDTO dto = new StudentGradeDTO();
        dto.setStudentId(1L);
        dto.setCourseId(2L);

        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        when(courseRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<StudentRecursionDTO> result = studentService.addGradeToStudent(dto);
        assertTrue(result.isEmpty());
    }
}
