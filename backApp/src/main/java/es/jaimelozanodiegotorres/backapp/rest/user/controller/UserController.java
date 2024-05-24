package es.jaimelozanodiegotorres.backapp.rest.user.controller;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.commons.controller.CommonController;
import es.jaimelozanodiegotorres.backapp.rest.user.dto.UserDto;
import es.jaimelozanodiegotorres.backapp.rest.user.filters.UserFilters;
import es.jaimelozanodiegotorres.backapp.rest.user.models.User;
import es.jaimelozanodiegotorres.backapp.rest.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controlador de usuarios
 * Anotado con @RestController para indicar que es un controlador
 * Anotado con @RequestMapping para indicar la ruta de acceso
 * Anotado con @PreAuthorize para indicar que solo los usuarios con rol USER pueden acceder
 */
@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("users")
@Slf4j
@Tag(name = "Usuarios", description = "Endpoint usuarios de la tienda")
public class UserController extends CommonController<User, UUID, UserDto> {
    UserService service;

    /**
     * Constructor de la clase
     *
     * @param service Servicio de productos
     */
    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @Override
    public List<User> listAll() {
        log.info("Listando todos los usuarios");
        return service.listAll();
    }

    @GetMapping("pageAll")
    public ResponseEntity<PageResponse<User>> pageAll(@Valid UserFilters filters) {
        return ResponseEntity.ok(service.pageAll(filters));
    }

    @Override
    @GetMapping("{id}")
    public ResponseEntity<User> findById(@PathVariable UUID id) {
        log.info("Buscando usuario con id: {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("saveUser")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> save(@RequestBody @Valid UserDto dto) {
        log.info("Guardando usuario");
        return ResponseEntity.ok(service.save(dto));
    }

    @Override
    @PutMapping("updateUser/{id}")
    public ResponseEntity<User> update(@PathVariable UUID id,@RequestBody @Valid UserDto dto) {
        log.info("Actualizando usuario con id {} y datos: {}" , id, dto);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Override
    @DeleteMapping("deleteUser/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Boolean> deleteById(@PathVariable UUID id) {
        log.info("Borrando valoracion con id {}", id);
        return ResponseEntity.ok(service.deleteByIdLogico(id));
    }
}
