package es.jaimelozanodiegotorres.backapp.rest.products.controller;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import es.jaimelozanodiegotorres.backapp.rest.products.services.ProductService;
import es.jaimelozanodiegotorres.backapp.rest.products.services.ProductServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador de la entidad Product
 * Anotación @RestController para indicar que es un controlador
 */
@RestController
@Slf4j
public class ProductController {
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

    @GetMapping("listAllProducts")
    List<Product>listAllProducts(){
        log.info("Buscando todos los productos");
        return service.listAll();
    }

    /**
     * Método que obtiene todos los productos que cumplan con los parámetros de búsqueda
     *
     * @param nombre     Nombre del producto
     * @param stockMax   Stock máximo del producto
     * @param stockMin   Stock mínimo del producto
     * @param precioMax  Precio máximo del producto
     * @param precioMin  Precio mínimo del producto
     * @param gluten     indica si el producto tiene gluten
     * @param deletedAt indica si el producto está eliminado
     * @param page       Número de página
     * @param size       Tamaño de la página
     * @param sortBy     Campo de ordenación
     * @param direction  Dirección de ordenación
     * @return Página de productos que cumplan con los parámetros de búsqueda
     */
    @GetMapping("/productos")
    public ResponseEntity<PageResponse<Product>> getProducts(
            @RequestParam(required = false) Optional<String> nombre,
            @RequestParam(required = false) Optional<Integer> stockMax,
            @RequestParam(required = false) Optional<Integer> stockMin,
            @RequestParam(required = false) Optional<Double> precioMax,
            @RequestParam(required = false) Optional<Double> precioMin,
            @RequestParam(required = false) Optional<Boolean> gluten,
            @RequestParam(required = false, defaultValue = "false") Optional<Boolean> deletedAt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<Product> pageResult = service.findAll(nombre, stockMax, stockMin, precioMax, precioMin, gluten, deletedAt, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    /**
     * Obtiene un producto por su ID
     *
     * @param id ID del producto a buscar
     * @return Producto que coincida con el ID
     */
    @GetMapping("/productos/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Manejador de excepciones para los errores de validación
     *
     * @param ex Excepción de validación
     * @return Mapa con los errores de validación
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
