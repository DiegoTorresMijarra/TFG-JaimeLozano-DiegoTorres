package es.jaimelozanodiegotorres.backapp.rest.category.services;

import es.jaimelozanodiegotorres.backapp.rest.category.dto.CategorySaveDto;
import es.jaimelozanodiegotorres.backapp.rest.category.dto.CategoryUpdateDto;
import es.jaimelozanodiegotorres.backapp.rest.category.exceptions.CategoryNotFound;
import es.jaimelozanodiegotorres.backapp.rest.category.mappers.CategoryMapper;
import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;
import es.jaimelozanodiegotorres.backapp.rest.category.repositories.CategoryCrudRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementación del servicio de Posiciones
 */
@Service
@Slf4j
@Cacheable(cacheNames = {"categorias"})
public class CategoryServiceImp implements CategoryService{
    private final CategoryCrudRepository categoryCrudRepository;

    @Autowired
    public CategoryServiceImp(CategoryCrudRepository categoryCrudRepository) {
        log.info("Iniciando el Servicio de Categorias");
        this.categoryCrudRepository = categoryCrudRepository;
    }

    @Override
    public Page<Category> findAll(Optional<String> name, Pageable pageable) {
        log.info("Buscando todas las Categorias");
        //Criterio por nombre
        Specification<Category> specName=(root, query, criteriaBuilder)->
                name.map(d->criteriaBuilder.equal(root.get("nombre"),d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Category> specification=Specification.where(specName);
        return categoryCrudRepository.findAll(specification,pageable);
    }

    @Override
    @Cacheable(key="#id")
    public Category findById(Long id) {
        log.info("Buscando la Categoria con el id: " + id);
        return categoryCrudRepository.findById(id).orElseThrow(() -> new CategoryNotFound(id));
    }

    @Override
    @Cacheable(key="#result.id")
    public Category save(CategorySaveDto category) {
        log.info("Guardando la Categoria con el nombre: " + category.getName());
        return categoryCrudRepository.save(CategoryMapper.toModel(category));
    }

    @Override
    @Transactional
    @Cacheable(key="#result.id")
    public Category update(Long id, CategoryUpdateDto category) {
        log.info("Actualizando la Categoria con el id: " + id);
        Category original = findById(id);
        return categoryCrudRepository.save(CategoryMapper.toModel(original,category));
    }

    @Override
    public void deleteById(Long id) {
        log.info("Eliminando la Categoria con el id: " + id);
        Category original = findById(id);
        original.setDeletedAt(LocalDateTime.now());
        categoryCrudRepository.delete(original);
    }
}
