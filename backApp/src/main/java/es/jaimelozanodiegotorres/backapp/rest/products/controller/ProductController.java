package es.jaimelozanodiegotorres.backapp.rest.products.controller;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.commons.controller.CommonController;
import es.jaimelozanodiegotorres.backapp.rest.products.dto.ProductSaveDto;
import es.jaimelozanodiegotorres.backapp.rest.products.filters.ProductFilters;
import es.jaimelozanodiegotorres.backapp.rest.products.filters.ProductFiltersDto;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import es.jaimelozanodiegotorres.backapp.rest.products.services.ProductServicePgSqlImp;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controlador de la entidad Product
 * Anotación @RestController para indicar que es un controlador
 */
@RestController
@RequestMapping("products")
@Slf4j
public class
ProductController extends CommonController<Product, Long, ProductSaveDto> {
    ProductServicePgSqlImp service;

    /**
     * Constructor de la clase
     *
     * @param service Servicio de productos
     */
    @Autowired
    public ProductController(ProductServicePgSqlImp service) {
        this.service = service;
    }

    @Override
    @GetMapping("listAll")
    public List<Product> listAll() {
        log.info("Listando todos los productos");
        return service.listAll();
    }

    @PostMapping("pageAll")
    public ResponseEntity<PageResponse<Product>> pageAll(@RequestBody(required = false) @Valid ProductFiltersDto productFiltersDto) {
        return ResponseEntity.ok(service.pageAll(productFiltersDto));
    }

    @Override
    @GetMapping("{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        log.info("Buscando producto con id: {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @Override
    @PostMapping("saveProduct")
    @PreAuthorize("hasAnyRole('WORKER','ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> save(@RequestBody @Valid ProductSaveDto dto) {
        log.info("Guardando producto");
        return ResponseEntity.ok(service.save(dto));
    }

    @PatchMapping(value = "updateProductPhoto/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('WORKER','ADMIN')")
    public ResponseEntity<Product> updateProductPhoto(@PathVariable Long id, @RequestParam("image") MultipartFile image) {
        log.info("Actualizando imagen del producto");
        return ResponseEntity.ok(service.updateProductPhoto(id, image));
    }

    @Override
    @PutMapping("updateProduct/{id}")
    @PreAuthorize("hasAnyRole('WORKER','ADMIN')")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody @Valid ProductSaveDto dto) {
        log.info("Actualizando producto con id {} y datos: {}" , id, dto);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Override
    @DeleteMapping("deleteProduct/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        log.info("Borrando producto con id {}", id);
        return ResponseEntity.ok(service.deleteById(id));
    }
}
