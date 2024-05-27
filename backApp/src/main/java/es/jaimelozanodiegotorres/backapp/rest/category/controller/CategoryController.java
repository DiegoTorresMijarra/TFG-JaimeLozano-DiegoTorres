package es.jaimelozanodiegotorres.backapp.rest.category.controller;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.category.dto.CategoryDto;
import es.jaimelozanodiegotorres.backapp.rest.category.filters.CategoryFilters;
import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;
import es.jaimelozanodiegotorres.backapp.rest.category.services.CategoryServiceImp;
import es.jaimelozanodiegotorres.backapp.rest.commons.controller.CommonController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador de la entidad Product
 * Anotación @RestController para indicar que es un controlador
 */
@RestController
@RequestMapping("categories")
@Slf4j
public class CategoryController extends CommonController<Category, Long, CategoryDto> {
    CategoryServiceImp service;

    /**
     * Constructor de la clase
     *
     * @param service Servicio de productos
     */
    @Autowired
    public CategoryController(CategoryServiceImp service) {
        this.service = service;
    }

    @Override
    @GetMapping("listAll")
    public List<Category> listAll() {
        log.info("Listando todas las categorias");
        return service.listAll();
    }

    @GetMapping("pageAll")
    public ResponseEntity<PageResponse<Category>> pageAll(@Valid CategoryFilters filters) {
        return ResponseEntity.ok(service.pageAll(filters));
    }

    @Override
    @GetMapping("{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id) {
        log.info("Buscando categoria con id: {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("{name}")
    public ResponseEntity<Category> findByName(@PathVariable String name){
        log.info("Buscando Categoria con nombre: " + name);
        return ResponseEntity.ok(service.findByName(name));
    }

    @PostMapping("saveCategory")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Category> save(@RequestBody @Valid CategoryDto dto) {
        log.info("Guardando categoria");
        return ResponseEntity.ok(service.save(dto));
    }

    @Override
    @PutMapping("updateCategory/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> update(@PathVariable Long id, @RequestBody @Valid CategoryDto dto) {
        log.info("Actualizando categoria con id {} y datos: {}" , id, dto);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Override
    @DeleteMapping("deleteCategory/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        log.info("Borrando categoria con id {}", id);
        return ResponseEntity.ok(service.deleteById(id));
    }
}
