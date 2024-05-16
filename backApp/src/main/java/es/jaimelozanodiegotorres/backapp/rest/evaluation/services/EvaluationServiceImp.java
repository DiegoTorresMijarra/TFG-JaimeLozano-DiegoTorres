package es.jaimelozanodiegotorres.backapp.rest.evaluation.services;

import es.jaimelozanodiegotorres.backapp.rest.category.exceptions.CategoryNotFound;
import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;
import es.jaimelozanodiegotorres.backapp.rest.category.repositories.CategoryCrudRepository;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationSaveDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationUpdateDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.exceptions.EvaluationNotFound;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.mapper.EvaluationMapper;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.models.Evaluation;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.repository.EvaluationRepository;
import es.jaimelozanodiegotorres.backapp.rest.products.exceptions.ProductNotFound;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import es.jaimelozanodiegotorres.backapp.rest.products.services.ProductServiceImp;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementación del servicio de Posiciones
 */
@Service
@Slf4j
@Cacheable(cacheNames = {"valoraciones"})
public class EvaluationServiceImp implements EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final ProductServiceImp productService;

    @Autowired
    public EvaluationServiceImp(EvaluationRepository evaluationRepository, ProductServiceImp productService) {
        log.info("Iniciando el Servicio de valoraciones");
        this.evaluationRepository = evaluationRepository;
        this.productService = productService;
    }


    @Override
    public Page<Evaluation> findAll(Optional<Integer> ValorMin, Optional<Integer> ValorMax, Pageable pageable) {
        log.info("Buscando todas las valoraciones");
        //Criterio por valoracion maximo
        Specification<Evaluation> specValorMax= (root, query, criteriaBuilder) ->
                ValorMax.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("valoracion"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        //Criterio por valoracion minimo
        Specification<Evaluation> specValorMin= (root, query, criteriaBuilder) ->
                ValorMin.map(p -> criteriaBuilder.greaterThanOrEqualTo(root.get("valoracion"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Evaluation> specification=Specification.where(specValorMax).and(specValorMin);
        return evaluationRepository.findAll(specification,pageable);
    }

    @Override
    @Cacheable(key="#id")
    public Evaluation findById(Long id) {
        log.info("Buscando la Valoracion con el id: " + id);
        return evaluationRepository.findById(id).orElseThrow(() -> new EvaluationNotFound(id));
    }

    @Override
    public Page<Evaluation> findByProductId(Long productId, Optional<Integer> ValorMin, Optional<Integer> ValorMax, Pageable pageable) {
        log.info("Buscando todas las valoraciones por producto");
        //Criterio por valoracion maximo
        Specification<Evaluation> specValorMax= (root, query, criteriaBuilder) ->
                ValorMax.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("valoracion"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        //Criterio por valoracion minimo
        Specification<Evaluation> specValorMin= (root, query, criteriaBuilder) ->
                ValorMin.map(p -> criteriaBuilder.greaterThanOrEqualTo(root.get("valoracion"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Evaluation> specification=Specification.where(specValorMax).and(specValorMin);
        return evaluationRepository.findByProductId(productId,specification,pageable).orElseThrow(() -> new ProductNotFound(productId));
    }

    @Override
    public Evaluation save(EvaluationSaveDto evaluation) {
        log.info("Guardando valoracion");
        Product product = productService.findById(evaluation.getProductId());
        return evaluationRepository.save(EvaluationMapper.toModel(evaluation, product));
    }

    @Override
    @Transactional
    public Evaluation update(Long id, EvaluationUpdateDto evaluation) {
        log.info("Actualizando valoracion con id: " + id);
        Evaluation original= findById(id);

        return evaluationRepository.save(EvaluationMapper.toModel(original, evaluation));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Eliminando la valoracion con el id: " + id);
        Evaluation original = findById(id);
        original.setDeletedAt(LocalDateTime.now());
        evaluationRepository.save(original);
    }
}
