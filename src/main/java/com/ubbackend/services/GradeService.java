package com.ubbackend.services;

import com.ubbackend.DTO.*;
import com.ubbackend.model.GradeEntity;

import java.util.List;
import java.util.Optional;

public interface GradeService {
    List<GradeEntity> getGrades();
    boolean deleteGrade(Long id);
    Optional<GradeRecursionDTO> updateGrade(Long grade, Float value);
}
