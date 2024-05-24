package es.jaimelozanodiegotorres.backapp.rest.evaluation.services;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.commons.services.CommonService;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.filters.EvaluationFilters;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.mappers.EvaluationMapper;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.models.Evaluation;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.repository.EvaluationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = {"valoraciones"})
@Slf4j
public class EvaluationServiceImp extends CommonService<Evaluation, Long>{
    EvaluationMapper mapper;

    @Autowired
    public EvaluationServiceImp(EvaluationRepository repository){
        super(repository);
        this.mapper = EvaluationMapper.INSTANCE;
    }

    public Evaluation save(EvaluationDto dto){
        log.info("Guardando valoracion");
        return save(mapper.dtoToModel(dto));
    }

    public Evaluation update(Long id, EvaluationDto dto){
        log.info("Actualizando valoracion");
        Evaluation original = findById(id);
        return update(mapper.updateModel(original, dto));
    }

    public PageResponse<Evaluation> pageAll(EvaluationFilters filters){
        log.info("Paginando todas las valoraciones");
        Page<Evaluation> page = repository.findAll(filters.getSpecifications(), filters.getPageable());
        return PageResponse.of(page, filters.getSortBy(), filters.getDirection());
    }

    public PageResponse<Evaluation> findByProductId(Long productId,EvaluationFilters filters) {
        log.info("Buscando todas las valoraciones paginadas de un producto");
        Page<Evaluation> page = ((EvaluationRepository)repository).findByProductId(productId, filters.getSpecifications(), filters.getPageable()).orElseThrow(()-> exceptionService.notFoundException(productId.toString()));
        return PageResponse.of(page, filters.getSortBy(), filters.getDirection());
    }
    public List<Evaluation> findByProductId(Long productId) {
        log.info("Buscando todas las valoraciones de un producto");
        return ((EvaluationRepository)repository).findByProductId(productId).orElseThrow(()-> exceptionService.notFoundException(productId.toString()));
    }
}
