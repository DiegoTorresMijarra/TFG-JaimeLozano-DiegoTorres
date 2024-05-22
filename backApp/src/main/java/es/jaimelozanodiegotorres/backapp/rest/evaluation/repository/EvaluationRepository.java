package es.jaimelozanodiegotorres.backapp.rest.evaluation.repository;

import es.jaimelozanodiegotorres.backapp.rest.commons.repository.CommonRepository;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.models.Evaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository extends CommonRepository<Evaluation,Long>{
    @Override
    default String getTableName(){
        return "evaluation";
    }

    Optional<Page<Evaluation>> findByProductId(Long product_id, Specification<Evaluation> specification, Pageable pageable);
    Optional<List<Evaluation>> findByProductId(Long product_id);
}
