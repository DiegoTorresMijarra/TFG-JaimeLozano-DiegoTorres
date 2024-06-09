package es.jaimelozanodiegotorres.backapp.rest.evaluation.services;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.commons.services.CommonServicePgSql;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationResponseDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.filters.EvaluationFilters;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.mappers.EvaluationResponseMapper;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.mappers.EvaluationSaveMapper;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.models.Evaluation;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.repository.EvaluationRepository;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import es.jaimelozanodiegotorres.backapp.rest.products.services.ProductServicePgSqlImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = {"valoraciones"})
@Slf4j
public class EvaluationServicePgSqlImp extends CommonServicePgSql<Evaluation, Long> {
    private final EvaluationSaveMapper evaluationSaveMapper;
    private final EvaluationResponseMapper evaluationResponseMapper;
    private final ProductServicePgSqlImp productServiceImp;

    @Autowired
    public EvaluationServicePgSqlImp(EvaluationRepository repository, ProductServicePgSqlImp productServiceImp){
        super(repository);
        this.evaluationSaveMapper = EvaluationSaveMapper.INSTANCE;
        this.evaluationResponseMapper = EvaluationResponseMapper.INSTANCE;
        this.productServiceImp = productServiceImp;
    }

    public Evaluation save(EvaluationDto dto){
        log.info("Guardando valoracion");
        Product product = productServiceImp.findById(dto.getProductId());
        Evaluation evaluation = evaluationSaveMapper.dtoToModel(dto);

        evaluation.setProduct(product);
        evaluation.setUser(getLoggedUser());

        return save(evaluation);
    }

    public Evaluation update(Long id, EvaluationDto dto){
        log.info("Actualizando valoracion");
        Evaluation original = findById(id);

        verifyLogguedSameUser(original.getUser());

        Product product = productServiceImp.findById(dto.getProductId());
        Evaluation evaluation = evaluationSaveMapper.updateModel(original, dto);
        evaluation.setProduct(product);

        return update(evaluation);
    }
/**
    public PageResponse<Evaluation> pageAll(EvaluationFilters filters){
        log.info("Paginando todas las valoraciones");
        Page<Evaluation> page = repository.findAll(filters.getSpecifications(), filters.getPageable());
        return PageResponse.of(page, filters.getSortBy(), filters.getDirection());
    }
 */
/**
    public PageResponse<Evaluation> findByProductId(Long productId, EvaluationFilters filters) {
        log.info("Buscando todas las valoraciones paginadas de un producto");
        Page<Evaluation> page = ((EvaluationRepository)repository).findByProductId(productId, filters.getSpecifications(), filters.getPageable()).orElseThrow(()-> exceptionService.notFoundException(productId.toString()));
        return PageResponse.of(page, filters.getSortBy(), filters.getDirection());
    }
*/
    public List<EvaluationResponseDto> findByProductId(Long productId) {
        log.info("Buscando todas las valoraciones de un producto");

        return evaluationResponseMapper.modelToDto(
                ((EvaluationRepository)repository).findByProductId(productId));
    }
}
