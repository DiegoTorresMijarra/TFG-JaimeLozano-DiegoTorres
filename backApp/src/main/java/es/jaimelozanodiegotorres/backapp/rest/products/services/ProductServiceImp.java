package es.jaimelozanodiegotorres.backapp.rest.products.services;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.commons.services.CommonService;
import es.jaimelozanodiegotorres.backapp.rest.products.dto.ProductSaveDto;
import es.jaimelozanodiegotorres.backapp.rest.products.filters.ProductFilters;
import es.jaimelozanodiegotorres.backapp.rest.products.mapper.ProductMapper;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import es.jaimelozanodiegotorres.backapp.rest.products.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Implementación de la interfaz ProductService.
 * Se utiliza la anotación @Service para indicar que es un servicio de Spring.
 * Se utiliza la anotación @CacheConfig para indicar el nombre de la caché.
 */
@Service
@CacheConfig(cacheNames = {"productos"})
@Slf4j
public class ProductServiceImp extends CommonService<Product, Long> {
    ProductMapper mapper;
    /**
     * Constructor de la clase
     * @param repository Repositorio de productos
     * (se utiliza la anotación @Autowired para indicar que se trata de una inyección de dependencia).
     */
    @Autowired
    public ProductServiceImp(ProductRepository repository){
        super(repository);
        this.mapper = ProductMapper.INSTANCE;
    }

    public Product save(ProductSaveDto dto){
        return save(mapper.dtoToModel(dto));
    }

    public Product update(Long id, ProductSaveDto dto){
        Product original = findById(id);
        return update(mapper.updateModel(original, dto));
    }

    public PageResponse<Product> pageAll(ProductFilters filters){
        log.info("Paginando todos los productos");
        Page<Product> page = repository.findAll(filters.getSpecifications(), filters.getPageable());
        return PageResponse.of(page, filters.getSortBy(), filters.getDirection());
    }
}
