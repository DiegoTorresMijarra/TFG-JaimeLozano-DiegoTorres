package es.jaimelozanodiegotorres.backapp.rest.offers.controller;

import es.jaimelozanodiegotorres.backapp.rest.commons.controller.CommonController;
import es.jaimelozanodiegotorres.backapp.rest.offers.dto.OfferDto;
import es.jaimelozanodiegotorres.backapp.rest.offers.models.Offer;
import es.jaimelozanodiegotorres.backapp.rest.offers.service.OfferServicePgSqlImp;
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
@RequestMapping("offers")
@Slf4j
public class OfferController extends CommonController<Offer, Long, OfferDto>{
    OfferServicePgSqlImp service;

    /**
     * Constructor de la clase
     *
     * @param service Servicio de productos
     */
    @Autowired
    public OfferController(OfferServicePgSqlImp service) {
        this.service = service;
    }

    @Override
    @GetMapping("listAll")
    public List<Offer> listAll() {
        log.info("Listando todas las ofertas");
        return service.listAll();
    }

    @GetMapping("active/{id}")
    public Offer ActiveByProductId(@PathVariable Long id) {
        log.info("Listando la oferta activa de un producto");
        return service.findActivasByProductId(id);
    }

    @Override
    @GetMapping("{id}")
    public ResponseEntity<Offer> findById(@PathVariable Long id) {
        log.info("Buscando oferta con id: {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @Override
    @PostMapping("saveOffer")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Offer> save(@RequestBody @Valid OfferDto dto) {
        log.info("Guardando oferta");
        return ResponseEntity.ok(service.save(dto));
    }

    @Override
    @PutMapping("updateOffer/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Offer> update(@PathVariable Long id,@RequestBody @Valid OfferDto dto) {
        log.info("Actualizando oferta con id {} y datos: {}" , id, dto);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Override
    @DeleteMapping("deleteOffer/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        log.info("Borrando oferta con id {}", id);
        return ResponseEntity.ok(service.deleteById(id));
    }
}
