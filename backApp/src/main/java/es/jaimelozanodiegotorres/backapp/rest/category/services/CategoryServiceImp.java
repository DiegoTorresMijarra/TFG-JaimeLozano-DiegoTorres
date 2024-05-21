package es.jaimelozanodiegotorres.backapp.rest.category.services;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.category.dto.CategoryDto;
import es.jaimelozanodiegotorres.backapp.rest.category.mappers.CategoryMapper;
import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;
import es.jaimelozanodiegotorres.backapp.rest.category.repository.CategoryRepository;
import es.jaimelozanodiegotorres.backapp.rest.commons.services.CommonService;
import es.jaimelozanodiegotorres.backapp.rest.category.filters.CategoryFilters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = {"categorias"})
@Slf4j
public class CategoryServiceImp extends CommonService<Category, Long> {
    CategoryMapper mapper;

    @Autowired
    public CategoryServiceImp(CategoryRepository repository){
        super(repository);
        this.mapper = CategoryMapper.INSTANCE;
    }

    public Category save(CategoryDto dto){
        log.info("Guardando categoria");
        return save(mapper.dtoToModel(dto));
    }

    public Category update(Long id, CategoryDto dto){
        log.info("Actualizando categoria");
        Category original = findById(id);
        return update(mapper.updateModel(original, dto));
    }

    public PageResponse<Category> pageAll(CategoryFilters filters){
        log.info("Paginando todas las categorias");
        Page<Category> page = repository.findAll(filters.getSpecifications(), filters.getPageable());
        return PageResponse.of(page, filters.getSortBy(), filters.getDirection());
    }

    @Cacheable(key="#name")
    public Category findByName(String name) {
        log.info("Buscando la Categoria con el nombre: " + name);
        return ((CategoryRepository)repository).findByName(name).orElseThrow( ()-> exceptionService.notFoundExceptionName(name));
    }
}
