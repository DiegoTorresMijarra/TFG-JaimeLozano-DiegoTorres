package es.jaimelozanodiegotorres.backapp.rest.products.controller;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.commons.controller.CommonController;
import es.jaimelozanodiegotorres.backapp.rest.commons.filters.CommonFilters;
import es.jaimelozanodiegotorres.backapp.rest.products.dto.ProductSaveDto;
import es.jaimelozanodiegotorres.backapp.rest.products.filters.ProductFilters;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import es.jaimelozanodiegotorres.backapp.rest.products.services.ProductServiceImp;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador de la entidad Product
 * Anotación @RestController para indicar que es un controlador
 */
@RestController
@RequestMapping("products")
@Slf4j
public class ProductController extends CommonController<Product, Long, ProductSaveDto> {
    ProductServiceImp service;

    /**
     * Constructor de la clase
     *
     * @param service Servicio de productos
     */
    @Autowired
    public ProductController(ProductServiceImp service) {
        this.service = service;
    }

    @Override
    @GetMapping("listAll")
    public List<Product> listAll() {
        log.info("Listando todos los productos");
        return service.listAll();
    }

    @GetMapping("pageAll")
    public ResponseEntity<PageResponse<Product>> pageAll(@Valid ProductFilters filters) {
        return ResponseEntity.ok(service.pageAll(filters));
    }

    @Override
    @GetMapping("{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        log.info("Buscando producto con id: {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @Override
    @PostMapping("saveProduct")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> save(@RequestBody @Valid ProductSaveDto dto) {
        log.info("Guardando producto");
        return ResponseEntity.ok(service.save(dto));
    }


    @Override
    @PutMapping("updateProduct/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody @Valid ProductSaveDto dto) {
        log.info("Actualizando producto con id {} y datos: {}" , id, dto);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Override
    @DeleteMapping("deleteProduct/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        log.info("Borrando producto con id {}", id);
        return ResponseEntity.ok(service.deleteById(id));
    }

    
}
