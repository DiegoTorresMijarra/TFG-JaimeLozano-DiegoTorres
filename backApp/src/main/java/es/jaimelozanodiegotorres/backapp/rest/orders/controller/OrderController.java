package es.jaimelozanodiegotorres.backapp.rest.orders.controller;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.commons.controller.CommonController;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.services.EvaluationServiceImp;
import es.jaimelozanodiegotorres.backapp.rest.orders.dto.OrderDto;
import es.jaimelozanodiegotorres.backapp.rest.orders.models.Order;
import es.jaimelozanodiegotorres.backapp.rest.orders.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Controlador de la entidad Product
 * Anotación @RestController para indicar que es un controlador
 */
@RestController
@RequestMapping("orders")
@Slf4j
public class OrderController extends CommonController<Order, ObjectId, OrderDto> {

    OrderService service;

    /**
     * Constructor de la clase
     *
     * @param service Servicio de productos
     */
    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    @Override
    @GetMapping("listAll")
    public List<Order> listAll() {
        log.info("Listando todos los pedidos");
        return service.listAll();
    }

    @GetMapping("pageAll")
    public ResponseEntity<PageResponse<Order>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("Obteniendo todos los pedidos pageados");
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Page<Order> pageResult = service.findAll(PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    @Override
    @GetMapping("{id}")
    public ResponseEntity<Order> findById(@PathVariable("id") ObjectId id) {
        log.info("Buscando pedido con id: {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @Override
    @PostMapping("saveOrder")
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Order> save(@RequestBody @Valid OrderDto dto) {
        log.info("Guardando pedido");
        return ResponseEntity.ok(service.save(dto));
    }

    @Override
    @PutMapping("updateOrder/{id}")
    @Transactional
    public ResponseEntity<Order> update(@PathVariable("id") ObjectId id,@RequestBody @Valid OrderDto dto) {
        log.info("Actualizando pedido con id {} y datos: {}" , id, dto);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Override
    @DeleteMapping("deleteOrder/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Boolean> deleteById(@PathVariable("id") ObjectId id) {
        log.info("Borrando pedido con id {}", id);
        return ResponseEntity.ok(service.deleteById(id));
    }

    @GetMapping("/restaurantExists/{id}")
    public ResponseEntity<Boolean> existsByRestaurantId(@PathVariable("id") Long id){
        log.info("Buscando si existe algun pedido con restaurante id: " + id);
        return ResponseEntity.ok(service.existsByRestaurantId(id));
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<PageResponse<Order>> findByRestaurantId(@PathVariable("id") Long id,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size,
                                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                                  @RequestParam(defaultValue = "asc") String direction
                                                                  ){
        log.info("Buscando los pedido pageados del del restaurante con id: " + id);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Page<Order> pageResult = service.findByRestaurantId(id,PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    @Transactional
    @PutMapping ("/isPaid/{id}")
    public ResponseEntity<Order> updateIsPaidById(
            @PathVariable (value = "id")  ObjectId id,
            @RequestParam  (value = "isPaid",required = true) Boolean isPaid){
        log.info("Actualizando isPaid del pedido con id: "+id.toHexString()+" a "+ isPaid);
        return ResponseEntity.ok(service.updateIsPaidById(id, isPaid));
    }
}