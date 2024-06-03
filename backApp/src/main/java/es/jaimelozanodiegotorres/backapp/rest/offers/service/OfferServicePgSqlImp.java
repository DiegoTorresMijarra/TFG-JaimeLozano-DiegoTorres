package es.jaimelozanodiegotorres.backapp.rest.offers.service;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.commons.services.CommonServicePgSql;
import es.jaimelozanodiegotorres.backapp.rest.offers.dto.OfferDto;
import es.jaimelozanodiegotorres.backapp.rest.offers.filters.OfferFilters;
import es.jaimelozanodiegotorres.backapp.rest.offers.mapper.OfferMapper;
import es.jaimelozanodiegotorres.backapp.rest.offers.models.Offer;
import es.jaimelozanodiegotorres.backapp.rest.offers.repository.OfferRepository;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import es.jaimelozanodiegotorres.backapp.rest.products.services.ProductServicePgSqlImp;
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
    private final ProductServicePgSqlImp productServiceImp;

    @Autowired
    public OfferServicePgSqlImp(OfferRepository repository, ProductServicePgSqlImp productServiceImp){
        super(repository);
        this.mapper = OfferMapper.INSTANCE;
        this.productServiceImp = productServiceImp;
    }

    public Offer save(OfferDto dto){
        log.info("Guardando oferta");
        Product product = productServiceImp.findById(dto.getProductId());
        Offer offer = mapper.dtoToModel(dto);
        offer.setProduct(product);
        return save(offer);
    }

    public Offer update(Long id, OfferDto dto){
        log.info("Actualizando oferta");
        Offer original = findById(id);
        Product product = productServiceImp.findById(dto.getProductId());
        Offer offer = mapper.updateModel(original, dto);
        offer.setProduct(product);
        return update(offer);
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
