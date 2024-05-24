package es.jaimelozanodiegotorres.backapp.rest.restaurants.controllers;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.commons.controller.CommonController;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.dto.RestaurantDto;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.modelos.Restaurant;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.servicios.RestaurantServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador de la entidad Restaurant
 * Anotamos la clase con @RestController para indicar que es un controlador
 * @Parameter service Servicio de restaurantes
 */
@RestController
@RequestMapping("restaurants")
@Tag(name = "Restaurantes", description = "Endpoint de Restaurantes de nuestra tienda")
@Slf4j
public class RestaurantController extends CommonController<Restaurant, Long, RestaurantDto> {
    RestaurantServiceImpl service;

    /**
     * Constructor de la clase
     * @param service Servicio de restaurantes
     */
    @Autowired
    public RestaurantController(RestaurantServiceImpl service){
        this.service=service;
    }

    /**
     * Método que obtiene todos los restaurantes que cumplan con los parámetros de búsqueda
     * @param name Opcional: nombre del restaurante
     * @param number Opcional: número de teléfono del restaurante
     * @param isDeleted Opcional: indica si el restaurante está eliminado
     * @param page numero de página a recuperar
     * @param size tamaño de la página
     * @param sortBy campo por el que ordenar
     * @param direction dirección de la ordenación
     * @return ResponseEntity con una pagina de restaurantes que cumplan con los parámetros de búsqueda
     */
    @Operation(summary = "Obtiene todos los restaurantes que cumplan con los parámetros de búsqueda")
    @Parameters({
            @Parameter(name = "name", description = "Filtrar por nombre", example = "Restaurante"),
            @Parameter(name = "number", description = "Filtrar por nº de teléfono", example = "123456789"),
            @Parameter(name = "isDeleted", description = "Filtrar por restaurante eliminado", example = "false"),
            @Parameter(name = "page", description = "Número de página a recuperar", example = "0"),
            @Parameter(name = "size", description = "Tamaño de la página", example = "10"),
            @Parameter(name = "sortBy", description = "Campo por el que ordenar", example = "id"),
            @Parameter(name = "direction", description = "Dirección de la ordenación", example = "asc"),
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de restaurantes que cumplan con los parámetros de búsqueda"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error en los parámetros de búsqueda"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante no encontrado"),
    })
    @GetMapping("/pageAll")
    public ResponseEntity<PageResponse<Restaurant>> pageAll(
            @RequestParam(required=false) Optional<String> name,
            @RequestParam(required = false)Optional<String> number,
            @RequestParam(defaultValue = "false",required = false)Optional<Boolean> isDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
            ){
        Sort sort=direction.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Page<Restaurant> pageResult= service.pageAll(name, number,isDeleted, PageRequest.of(page,size,sort));
        return ResponseEntity.ok().body(PageResponse.of(pageResult,sortBy,direction));
    }


    @GetMapping("listAll")
    @Override
    public List<Restaurant> listAll() {
        log.info("Obteniendo todos los restaurantes");
        return service.listAll();
    }

    /**
     * Obtiene un restaurante por su ID
     * @param id ID del restaurante a buscar
     * @return Restaurante que coincida con el ID
     */
    @Operation(summary = "Obtiene un restaurante por su ID")
    @Parameter(name = "id", description = "ID del restaurante a buscar", example = "1")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante no encontrado"),
    })
    @GetMapping ("/{id}")
    @Override
    public ResponseEntity<Restaurant> findById(@PathVariable Long id){
        log.info("Buscando Restaurante con id: {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Guarda un restaurante
     * @param dto RestauranteDTO con la informacion del restaurante a guardar
     * @return Restaurante guardado
     */
    @Operation(summary = "Guarda un restaurante con la información de un RestauranteDTO")
    @Parameter(name = "dto", description = "RestauranteDTO con información del restaurante a guardar", required = true)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Restaurante guardado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error en los parámetros del Restaurante"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Restaurante a guardar", required=true)
    @PostMapping("/saveRestaurant")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<Restaurant> save(@Valid @RequestBody RestaurantDto dto){
        log.info("Listando todos los restaurantes");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    /**
     * Actualiza un restaurante
     * @param id ID del restaurante a actualizar
     * @param dto RestauranteDTO con la información a actualizar
     * @return Restaurante actualizado
     */
    @Operation(summary = "Actualiza un restaurante con la información de un RestauranteDTO")
    @Parameters({
            @Parameter(name = "id", description = "ID del restaurante a actualizar", example = "1",required = true),
            @Parameter(name = "restaurantDTO", description = "RestauranteDTO con información actualizada", required = true)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Restaurante a actualizar", required=true)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurante actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error en los parámetros del Restaurante"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante no encontrado"),
    })
    @PutMapping("updateRestaurant/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<Restaurant> update(@PathVariable Long id, @Valid @RequestBody RestaurantDto dto){
        log.info("Actualizando Restaurante con id: {}", id);
       return ResponseEntity.ok(service.update(id, dto));
    }

    /**
     * Elimina un restaurante
     * @param id ID del restaurante a eliminar
     */
    @Operation(summary = "Elimina un restaurante por su ID")
    @Parameter(name = "id", description = "ID del restaurante a eliminar", example = "1",required = true)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Restaurante eliminado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @DeleteMapping("deleteRestaurant/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id){
        log.info("Eliminando Restaurante con id: {}", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
