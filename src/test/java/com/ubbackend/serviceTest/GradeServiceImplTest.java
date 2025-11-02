package com.ubbackend.serviceTest;

import com.ubbackend.DTO.GradeRecursionDTO;
import com.ubbackend.model.GradeEntity;
import com.ubbackend.repository.GradeRepository;
import com.ubbackend.servicesImpl.GradeServiceImpl;
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

public class GradeServiceImplTest {

    @Mock
    private GradeRepository gradeRepository;

    @InjectMocks
    private GradeServiceImpl gradeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ------------------------------------------------------------------
    // getGrades()
    // ------------------------------------------------------------------
    @Test
    void testGetGrades_ReturnsList() {
        GradeEntity grade1 = new GradeEntity();
        grade1.setId(1L);
        grade1.setGrade(8.5F);

        GradeEntity grade2 = new GradeEntity();
        grade2.setId(2L);
        grade2.setGrade(9.0F);

        when(gradeRepository.findAll()).thenReturn(Arrays.asList(grade1, grade2));

        List<GradeEntity> result = gradeService.getGrades();

        assertEquals(2, result.size());
        assertEquals(8.5F, result.get(0).getGrade());
        assertEquals(9.0F, result.get(1).getGrade());
        verify(gradeRepository, times(1)).findAll();
    }

    // ------------------------------------------------------------------
    // deleteGrade()
    // ------------------------------------------------------------------
    @Test
    void testDeleteGrade_Success() {
        GradeEntity grade = new GradeEntity();
        grade.setId(1L);
        grade.setGrade(7.5F);

        when(gradeRepository.findById(1L)).thenReturn(Optional.of(grade));

        boolean result = gradeService.deleteGrade(1L);

        assertTrue(result);
        verify(gradeRepository, times(1)).findById(1L);
        verify(gradeRepository, times(1)).delete(grade);
    }

    @Test
    void testDeleteGrade_NotFound_ReturnsFalse() {
        when(gradeRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = gradeService.deleteGrade(99L);

        assertFalse(result);
        verify(gradeRepository, times(1)).findById(99L);
        verify(gradeRepository, never()).delete(any());
    }

    // ------------------------------------------------------------------
    // updateGrade()
    // ------------------------------------------------------------------
    @Test
    void testUpdateGrade_Success() {
        GradeEntity existing = new GradeEntity();
        existing.setId(5L);
        existing.setGrade(6.0F);

        when(gradeRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(gradeRepository.save(any(GradeEntity.class))).thenAnswer(i -> i.getArgument(0));

        Optional<GradeRecursionDTO> result = gradeService.updateGrade(5L, 9.5F);

        assertTrue(result.isPresent());
        assertEquals(9.5F, result.get().getGrade());
        verify(gradeRepository, times(1)).findById(5L);
        verify(gradeRepository, times(1)).save(existing);
    }

    @Test
    void testUpdateGrade_NotFound_ReturnsEmpty() {
        when(gradeRepository.findById(5L)).thenReturn(Optional.empty());

        Optional<GradeRecursionDTO> result = gradeService.updateGrade(5L, 8.0F);

        assertTrue(result.isEmpty());
        verify(gradeRepository, times(1)).findById(5L);
        verify(gradeRepository, never()).save(any());
    }
}
