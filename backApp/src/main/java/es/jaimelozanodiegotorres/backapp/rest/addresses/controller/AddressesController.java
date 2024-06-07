package es.jaimelozanodiegotorres.backapp.rest.addresses.controller;

import es.jaimelozanodiegotorres.backapp.rest.addresses.dto.AddressSaveDto;
import es.jaimelozanodiegotorres.backapp.rest.addresses.models.Addresses;
import es.jaimelozanodiegotorres.backapp.rest.addresses.services.AddressesServicePgSqlImpl;
import es.jaimelozanodiegotorres.backapp.rest.commons.controller.CommonController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("addresses")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class AddressesController extends CommonController<Addresses, UUID, AddressSaveDto> {

    private final AddressesServicePgSqlImpl addressesService;

    @Autowired
    public AddressesController(AddressesServicePgSqlImpl addressesService) {
        this.addressesService = addressesService;
    }

    @Override
    @GetMapping("listAll")
    public List<Addresses> listAll() {
        log.info("Buscando todos las direcciones");
        return addressesService.listAll();
    }

    @Override
    @GetMapping("{uuid}")
    public ResponseEntity<Addresses> findById(@PathVariable UUID uuid) {
        log.info("Buscando la direccion con id: {}", uuid.toString());
        return ResponseEntity.ok(addressesService.findById(uuid));
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("saveAddress")
    public ResponseEntity<Addresses> save(@RequestBody @Valid AddressSaveDto dto) {
        log.info("Guardando direccion");
        return ResponseEntity.ok(addressesService.save(dto));
    }

    @Override
    @PutMapping("updateAddress/{uuid}")
    public ResponseEntity<Addresses> update(@PathVariable  UUID uuid, @RequestBody @Valid AddressSaveDto dto) {
        log.info("Actualizando la direccion con id: {}", uuid.toString());
        return ResponseEntity.ok(addressesService.update(uuid, dto));
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("deleteAddress/{uuid}")
    public ResponseEntity<Boolean> deleteById(@PathVariable UUID uuid) {
        log.info("Borreando la direccion con id: {}", uuid.toString());
        return ResponseEntity.ok(addressesService.deleteById(uuid));
    }
}
