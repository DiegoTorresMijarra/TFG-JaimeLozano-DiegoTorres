package es.jaimelozanodiegotorres.backapp.rest.evaluation.repository;

import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.models.Evaluation;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long>, JpaSpecificationExecutor<Evaluation> {
    /**
     * Busca las valoraciones de un producto
     * @param product_id  producto a busacr
     * @return valoraciones encontradas
     */
    //@Query("SELECT w FROM Evaluation w WHERE w.product_id LIKE :product_id")
    Optional<Page<Evaluation>> findByProductId(Long product_id, Specification<Evaluation> specification, Pageable pageable);
    Optional<Page<Evaluation>> findByProductId(Long product_id, Pageable pageable);
}
