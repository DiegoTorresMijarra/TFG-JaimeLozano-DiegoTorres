package es.jaimelozanodiegotorres.backapp.rest.user.controller;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.commons.controller.CommonController;
import es.jaimelozanodiegotorres.backapp.rest.user.dto.UserDto;
import es.jaimelozanodiegotorres.backapp.rest.user.dto.UserResponseDto;
import es.jaimelozanodiegotorres.backapp.rest.user.filters.UserFilters;
import es.jaimelozanodiegotorres.backapp.rest.user.models.User;
import es.jaimelozanodiegotorres.backapp.rest.user.service.UserServicePgSql;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    UserServicePgSql service;

    /**
     * Constructor de la clase
     *
     * @param service Servicio de productos
     */
    @Autowired
    public UserController(UserServicePgSql service) {
        this.service = service;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> listAll() {
        log.info("Listando todos los usuarios");
        return service.listAll();
    }

//    @GetMapping("pageAll")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<PageResponse<User>> pageAll(@Valid UserFilters filters) {
//        return ResponseEntity.ok(service.pageAll(filters));
//    }

    @Override
    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN','WORKER')")
    public ResponseEntity<User> findById(@PathVariable UUID id) {
        log.info("Buscando usuario con id: {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("saveUser")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> save(@RequestBody @Valid UserDto dto) {
        log.info("Guardando usuario");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @Override
    @PutMapping("updateUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> update(@PathVariable UUID id,@RequestBody @Valid UserDto dto) {
        log.info("Actualizando usuario con id {} y datos: {}" , id, dto);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Override
    @DeleteMapping("deleteUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Boolean> deleteById(@PathVariable UUID id) {
        log.info("Borrando valoracion con id {}", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // detalles por usuario:


    @GetMapping("me/details")
    @PreAuthorize("hasAnyRole('USER','ADMIN','WORKER')")
    public ResponseEntity<UserResponseDto> details(@AuthenticationPrincipal User user) {
        log.info("Buscando detalles del usuario");
        return ResponseEntity.ok(service.details(user));
    }
}
