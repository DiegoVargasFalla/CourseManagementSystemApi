package com.ubbackend.servicesImpl;


import com.ubbackend.DTO.GradeRecursionDTO;
import com.ubbackend.exception.ResourceNotFoundException;
import com.ubbackend.model.GradeEntity;
import com.ubbackend.repository.GradeRepository;
import com.ubbackend.services.GradeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;

    public GradeServiceImpl(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @Override
    public List<GradeEntity> getGrades() {
        return gradeRepository.findAll();
    }

    @Override
    public boolean deleteGrade(Long id) {
        Optional<GradeEntity> grade = gradeRepository.findById(id);
        if(grade.isPresent()) {
            gradeRepository.delete(grade.get());
            return true;
        }
        throw new ResourceNotFoundException("Note couldn't be deleted");
    }

    @Override
    public Optional<GradeRecursionDTO> updateGrade(Long grade, Float value) {
        Optional<GradeEntity> gradeEntityExisting = gradeRepository.findById(grade);
        if(gradeEntityExisting.isPresent()) {
            GradeEntity gradeEntity = gradeEntityExisting.get();
            gradeEntity.setGrade(value);
            gradeRepository.save(gradeEntity);
            GradeRecursionDTO gradeRecursionDTO = new GradeRecursionDTO();

            gradeRecursionDTO.setId(gradeEntity.getId());
            gradeRecursionDTO.setGrade(gradeEntity.getGrade());

            return Optional.of(gradeRecursionDTO);

        }
        return Optional.empty();
    }
}
