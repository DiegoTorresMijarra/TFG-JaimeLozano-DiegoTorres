package es.jaimelozanodiegotorres.backapp.rest.products.services;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;
import es.jaimelozanodiegotorres.backapp.rest.category.services.CategoryServicePgSqlImp;
import es.jaimelozanodiegotorres.backapp.rest.commons.services.CommonServicePgSql;
import es.jaimelozanodiegotorres.backapp.rest.products.dto.ProductSaveDto;
import es.jaimelozanodiegotorres.backapp.rest.products.filters.ProductFilters;
import es.jaimelozanodiegotorres.backapp.rest.products.filters.ProductFiltersDto;
import es.jaimelozanodiegotorres.backapp.rest.products.mapper.ProductMapper;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import es.jaimelozanodiegotorres.backapp.rest.products.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;

/**
 * Implementación de la interfaz ProductService.
 * Se utiliza la anotación @Service para indicar que es un servicio de Spring.
 * Se utiliza la anotación @CacheConfig para indicar el nombre de la caché.
 */
@Service
@CacheConfig(cacheNames = {"productos"})
@Slf4j
public class ProductServicePgSqlImp extends CommonServicePgSql<Product, Long> {
    private final ProductMapper mapper;
    private final CategoryServicePgSqlImp categoryService;
    /**
     * Constructor de la clase
     * @param repository Repositorio de productos
     * (se utiliza la anotación @Autowired para indicar que se trata de una inyección de dependencia).
     */
    @Autowired
    public ProductServicePgSqlImp(ProductRepository repository, CategoryServicePgSqlImp categoryService){
        super(repository);
        this.mapper = ProductMapper.INSTANCE;
        this.categoryService = categoryService;
    }

    public Product save(ProductSaveDto dto){
        Category category = categoryService.findById(dto.getCategoryId()); //lanza exc si no existe
        Product product = mapper.dtoToModel(dto);
        product.setCategory(category);
        return save(product);
    }

    public Product update(Long id, ProductSaveDto dto){
        Product original = findById(id);
        Category category = categoryService.findById(dto.getCategoryId()); //lanza exc si no existe
        Product product = mapper.updateModel(original,dto);
        product.setCategory(category);
        return update(product);
    }

    public PageResponse<Product> pageAll(ProductFiltersDto productFiltersDto){
        log.info("Paginando todos los productos");
        ProductFilters filters = ProductFilters.builder().build();

        if(productFiltersDto != null)
            filters = productFiltersDto.getProductFilters();

        Page<Product> page = repository.findAll(filters.getSpecifications(), filters.getPageable());
        return PageResponse.of(page, filters.getSortBy(), filters.getDirection());
    }

    public Product updateProductPhoto(Long id, MultipartFile image) {
        log.info("Actualizando la imagen del producto con id: {}", id);

        String contentType = image.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Tipo de imagen invalido: " + ALLOWED_IMAGE_TYPES);
        }
        Product original = findById(id);
        original.setImageExtension(contentType);
        try {
            InputStream inputStream = image.getInputStream();
            byte[] imageData = inputStream.readAllBytes();
            original.setImage(imageData);
        } catch (IOException e) {
            throw exceptionService.badRequestException();
        }

        return repository.save(original);
    }

}
