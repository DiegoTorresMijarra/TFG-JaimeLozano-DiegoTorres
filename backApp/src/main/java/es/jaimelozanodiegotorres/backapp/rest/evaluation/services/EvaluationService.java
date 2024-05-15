package es.jaimelozanodiegotorres.backapp.rest.evaluation.services;

import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationSaveDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationUpdateDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.models.Evaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EvaluationService {
    Page<Evaluation> findAll(Optional<Integer> ValorMin,Optional<Integer> ValorMax, Pageable pageable);

    Evaluation findById(Long id);

    Page<Evaluation> findByProductId(Long productId, Optional<Integer> ValorMin,Optional<Integer> ValorMax, Pageable pageable);

    Evaluation save(EvaluationSaveDto evaluation);

    Evaluation update(Long id, EvaluationUpdateDto evaluation);

    void deleteById(Long id);
}
