package es.jaimelozanodiegotorres.backapp.rest.orders.service;

import es.jaimelozanodiegotorres.backapp.rest.commons.services.CommonServiceMongo;
import es.jaimelozanodiegotorres.backapp.rest.orders.dto.OrderDto;
import es.jaimelozanodiegotorres.backapp.rest.orders.dto.OrderType;
import es.jaimelozanodiegotorres.backapp.rest.orders.mapper.OrderMapper;
import es.jaimelozanodiegotorres.backapp.rest.orders.models.Order;

import es.jaimelozanodiegotorres.backapp.rest.orders.models.OrderedProduct;
import es.jaimelozanodiegotorres.backapp.rest.orders.repository.OrderRepository;
import es.jaimelozanodiegotorres.backapp.rest.products.repository.ProductRepository;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.repositories.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@CacheConfig(cacheNames = {"orders"})
@Slf4j
public class OrderService extends CommonServiceMongo<Order, ObjectId> {
    OrderMapper mapper;
    private final ProductRepository productRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public OrderService(OrderRepository repository, ProductRepository productRepository, RestaurantRepository restaurantRepository){
        super(repository);
        this.productRepository = productRepository;
        this.restaurantRepository = restaurantRepository;
        this.mapper = OrderMapper.INSTANCE;
    }

    public Page<Order> findAll(Pageable pageable) {
        log.info("Listando todos los pedidos pageados");
        return repository.findAll(pageable);
    }

    @Transactional //no se si es necesario repetirlo en controller tb
    public Order save(OrderDto dto) {
        log.info("Guardando order : {}", dto);
        checkOrderIds(dto);
        checkOrderedProducts(dto);
        return save(mapper.dtoToModel(dto));
    }

    public Order update(ObjectId objectId, OrderDto dto) {
        log.info("Actualizando un pedido por su id: "+objectId.toHexString());
        if(dto.getOrderedProducts()!=null) {
            checkOrderedProducts(dto);
        }
        checkOrderIds(dto);
        var original = findById(objectId);
        var updated = mapper.updateModel(original,dto);
        updated.setId(objectId);
        return update(updated);
    }

    /**
     * Busca los pedidos de un restaurante y los pagina
     * @param restaurantId id del restaurante
     * @param pageable paginacion
     * @return pedidos del restaurante
     */
    public Page<Order> findByRestaurantId(Long restaurantId, Pageable pageable) {
        log.info("Buscando los pedidos del restaurante con id: "+restaurantId);
        return ((OrderRepository)repository).findByRestaurantId(restaurantId, pageable);
    }

    /**
     * Comprueba si existe algun pedido del restaurante
     * @param restaurantId id del restaurante
     * @return true si existe algun pedido del restaurante, false en caso contrario
     */
    public Boolean existsByRestaurantId(Long restaurantId) {
        log.info("Comprobando si existe algun pedido en el restaurante con id: "+restaurantId);
        return ((OrderRepository)repository).existsByRestaurantId(restaurantId);
    }

    /**
     * Actualiza si el pedido ha sido pagado
     * @param objectId id del pedido
     * @param isPaid indica si el pedido ha sido pagado
     * @return pedido actualizado
     */
    public Order updateIsPaidById(ObjectId objectId, Boolean isPaid) {
        log.info("Actualizando isPaid del pedido con id: "+objectId.toHexString()+" a "+ isPaid);
        var original=findById(objectId);
        original.setIsPaid(isPaid);
        return save(original);
    }

    /**
     * No hay que verificar que sea nula, porque esta es validada antes de ser checkeada. <br>
     * El save desde las validaciones de las constraints y update en el metodo <br>
     * Tras verificar actualiza la cantidad del producto, puede generar bad smell, corregir -> principio de unidad
     * @param order
     * @param <T>
     */
    public <T extends OrderType> void checkOrderedProducts(T order) {
        log.info("Validando la lista de productos del pedido {}",order);
        var list= order.getOrderedProducts();
        if(list.isEmpty()) {
            throw exceptionService.badRequestException("La lista de productos no puede ser vacia");
        }
        for(OrderedProduct orderedProduct : list){
            var repositoryProduct = productRepository.findById(orderedProduct.getProductId()).orElseThrow(()-> exceptionService.notFoundException(orderedProduct.getProductId().toString()));
            if(orderedProduct.getProductPrice()!=repositoryProduct.getPrecio()) {
                throw exceptionService.badRequestException("El precio del producto con id"+orderedProduct.getProductId()+" no coincide con el de la base de datos");
            }
            if(orderedProduct.getQuantity()>repositoryProduct.getStock()) {
                throw exceptionService.badRequestException("La cantidad del producto con id"+orderedProduct.getProductId()+" no coincide con el de la base de datos");
            }
            //Actualizamos la cantidad del producto en el repo
            repositoryProduct.setStock(repositoryProduct.getStock()-orderedProduct.getQuantity());
            productRepository.save(repositoryProduct);
            //Actualizamos el total del pedido
            orderedProduct.calculateTotalPrice();
        }
    }

    /**
     * Verifica que los ids del pedido sean validos
     * @param order pedido
     * @param <T>
     */
    public <T extends OrderType> void checkOrderIds(T order) {
        log.info("Validando las referencias del pedido {}",order);
        //UUID clientUUID = order.getClientUUID();
        //if (clientUUID!=null){
            //var client = clientsRepository.findById(clientUUID)
                    //.orElseThrow(()->new OrderBadRequest("El cliente con UUID "+clientUUID+" no existe"));
        //}
        //UUID workerUUID = order.getWorkerUUID();
        //if (workerUUID!=null){
            //var worker = workersCrudRepository.findById(workerUUID)
                    //.orElseThrow(()->new OrderBadRequest("El trabajador con UUID "+workerUUID+" no existe"));
        //}
        Long restaurantId = order.getRestaurantId();
        if (restaurantId!=null){
            var restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(()-> exceptionService.badRequestException("El restaurante con id "+restaurantId+" no existe"));
        }
    }
}
