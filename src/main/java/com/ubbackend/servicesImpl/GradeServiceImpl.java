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

    /**
     * metodo para eliminar una nota
     * @param id de la nota a eliminar
     * @return valor booleano (true) si se elimino la nota de lo contrario informa con un error si no existe esa nota
     */
    @Override
    public boolean deleteGrade(Long id) {
        Optional<GradeEntity> grade = gradeRepository.findById(id);
        if(grade.isPresent()) {
            gradeRepository.delete(grade.get());
            return true;
        }
        throw new ResourceNotFoundException("Grade couldn't be deleted");
    }

    /**
     * metodo para actualizar el valor de la nota
     * @param gradeId id de la calificacion a actualizar
     * @param value valor nuevo para actualizar el valor anterior de la calificación
     * @return Optional con la clase completa de la calificacion actualizada o empty si no se encontro la nota
     */
    @Override
    public Optional<GradeRecursionDTO> updateGrade(Long gradeId, Float value) {
        Optional<GradeEntity> gradeEntityExisting = gradeRepository.findById(gradeId);
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
