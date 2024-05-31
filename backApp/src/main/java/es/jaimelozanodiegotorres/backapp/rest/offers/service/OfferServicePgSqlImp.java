package es.jaimelozanodiegotorres.backapp.rest.offers.service;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.commons.services.CommonServicePgSql;
import es.jaimelozanodiegotorres.backapp.rest.offers.dto.OfferDto;
import es.jaimelozanodiegotorres.backapp.rest.offers.filters.OfferFilters;
import es.jaimelozanodiegotorres.backapp.rest.offers.mapper.OfferMapper;
import es.jaimelozanodiegotorres.backapp.rest.offers.models.Offer;
import es.jaimelozanodiegotorres.backapp.rest.offers.repository.OfferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = {"ofertas"})
@Slf4j
public class OfferServicePgSqlImp extends CommonServicePgSql<Offer, Long> {
    OfferMapper mapper;

    @Autowired
    public OfferServicePgSqlImp(OfferRepository repository){
        super(repository);
        this.mapper = OfferMapper.INSTANCE;
    }

    public Offer save(OfferDto dto){
        log.info("Guardando oferta");
        return save(mapper.dtoToModel(dto));
    }

    public Offer update(Long id, OfferDto dto){
        log.info("Actualizando oferta");
        Offer original = findById(id);
        return update(mapper.updateModel(original, dto));
    }

    public PageResponse<Offer> pageAll(OfferFilters filters){
        log.info("Paginando todas las ofertas");
        Page<Offer> page = repository.findAll(filters.getSpecifications(), filters.getPageable());
        return PageResponse.of(page, filters.getSortBy(), filters.getDirection());
    }

    public Offer findActivasByProductId(Long productId) {
        log.info("Buscando la oferta activa de un producto");
        return ((OfferRepository)repository).findActivasByProduct(productId).orElseThrow(()-> exceptionService.notFoundException(productId.toString()));
    }
}
