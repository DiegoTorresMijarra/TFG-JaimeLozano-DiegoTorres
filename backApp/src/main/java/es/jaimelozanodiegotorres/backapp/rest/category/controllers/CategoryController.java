package es.jaimelozanodiegotorres.backapp.rest.category.controllers;


import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.category.dto.CategoryResponseDto;
import es.jaimelozanodiegotorres.backapp.rest.category.dto.CategorySaveDto;
import es.jaimelozanodiegotorres.backapp.rest.category.dto.CategoryUpdateDto;
import es.jaimelozanodiegotorres.backapp.rest.category.mappers.CategoryMapper;
import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;
import es.jaimelozanodiegotorres.backapp.rest.category.services.CategoryServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
//@PreAuthorize("hasRole('USER')")
@RequestMapping(value = "/categorias")
@Tag(name = "Categorias", description = "Endpoint de Categorias de nuestros productos")
public class CategoryController {
    private final CategoryServiceImp categoryService;

    @Autowired
    public CategoryController(CategoryServiceImp categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Obtiene todas las categorias
     * @param name opcional: nombre de la categoria
     * @param page numero de pagina
     * @param size tamaño de la pagina
     * @param sortBy campo por el que se ordena
     * @param direction ascendente o descendente
     * @return Page de las categorias que coinciden con los parametros
     */
    @Operation(summary = "Obtiene todas las categorias", description = "Obtiene todas las categorias")
    @Parameters({
            @Parameter(name = "name", description = "Nombre de la categoria", example = "Postre"),
            @Parameter(name = "page", description = "Pagina", example = "0"),
            @Parameter(name = "size", description = "Tamanio de la pagina", example = "10"),
            @Parameter(name = "sortBy", description = "Ordenamiento", example = "id"),
            @Parameter(name = "direction", description = "Ascendente o descendente", example = "asc")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categorias"),
            @ApiResponse(responseCode = "404", description = "Categorias no encontradas")
    })
    @GetMapping()
    public ResponseEntity<PageResponse<Category>> findAll(
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ){
        log.info("Buscando Categorias");
        Sort sort= direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<Category> pageResult = categoryService.findAll(name, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    /**
     * Obtiene una categoria por su id
     * @param id id de la categoria a obtener
     * @return Categoria con ese id
     */
    @Operation(summary = "Obtiene una categoria por su id", description = "Obtiene una categoria por su id")
    @Parameters({
            @Parameter(name = "id", description = "Identificador de la categoria", example = "1", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id){
        log.info("Buscando Categoria con id: " + id);
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @GetMapping("/{name}")
    public ResponseEntity<Category> findByName(@PathVariable String name){
        log.info("Buscando Categoria con nombre: " + name);
        return ResponseEntity.ok(categoryService.findByName(name));
    }

    /**
     * Crea una nueva categoria
     * @param category categoria con los datos a crear
     * @return categoria creada
     */
    @Operation(summary = "Crea una nueva categoria", description = "Crea una nueva categoria")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Categoria a crear", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Creado"),
            @ApiResponse(responseCode = "400", description = "Error de validacion")
    })
    @PostMapping("")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity <CategoryResponseDto> createPosition(@Valid @RequestBody CategorySaveDto category){
        log.info("Guardando Categoria con nombre: " + category.getName());
        return ResponseEntity.ok(CategoryMapper.toPositionResponseDto(categoryService.save(category)));
    }

    @Operation(summary = "Actualiza una categoria", description = "Actualiza una categoria")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Categoria a actualizar", required = true)
    @Parameters({
            @Parameter(name = "id", description = "Identificador de la categoria", example = "1", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualizado"),
            @ApiResponse(responseCode = "400", description = "Error de validacion"),
            @ApiResponse(responseCode = "404", description = "No encontrado"),
            @ApiResponse(responseCode= "403", description = "No autorizado")
    })
    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDto> updatePosition(@PathVariable Long id, @Valid @RequestBody CategoryUpdateDto position){
        log.info("Actualizando Posicion con id: " + id);
        return ResponseEntity.ok(CategoryMapper.toPositionResponseDto(categoryService.update(id, position)));
    }

    /**
     * Elimina una categoria
     * @param id id de la categoria a eliminar
     */
    @Operation(summary = "Elimina una categoria", description = "Elimina una categoria")
    @Parameters({
            @Parameter(name = "id", description = "Identificador de la categoria", example = "1", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Eliminado"),
            @ApiResponse(responseCode = "404", description = "No encontrado"),
            @ApiResponse(responseCode= "403", description = "No autorizado")
    })
    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public void deletePosition(@PathVariable Long id){
        log.info("Eliminando Posicion con Id: " + id);
        categoryService.deleteById(id);
    }

    /**
     * Maneja las excepciones de validación
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
