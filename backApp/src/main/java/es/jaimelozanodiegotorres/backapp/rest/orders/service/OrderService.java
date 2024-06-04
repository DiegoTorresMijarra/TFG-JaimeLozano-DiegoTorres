package es.jaimelozanodiegotorres.backapp.rest.orders.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.jaimelozanodiegotorres.backapp.config.websocket.WebSocketConfig;
import es.jaimelozanodiegotorres.backapp.config.websocket.WebSocketHandler;
import es.jaimelozanodiegotorres.backapp.rest.addresses.models.Addresses;
import es.jaimelozanodiegotorres.backapp.rest.addresses.services.AddressesServicePgSqlImpl;
import es.jaimelozanodiegotorres.backapp.rest.commons.services.CommonServiceMongo;
import es.jaimelozanodiegotorres.backapp.rest.orders.dto.OrderDto;
import es.jaimelozanodiegotorres.backapp.rest.orders.dto.OrderType;
import es.jaimelozanodiegotorres.backapp.rest.orders.mapper.OrderMapper;
import es.jaimelozanodiegotorres.backapp.rest.orders.models.Order;

import es.jaimelozanodiegotorres.backapp.rest.orders.models.OrderState;
import es.jaimelozanodiegotorres.backapp.rest.orders.models.OrderedProduct;
import es.jaimelozanodiegotorres.backapp.rest.orders.repository.OrderRepository;
import es.jaimelozanodiegotorres.backapp.rest.orders.websocket.Notificacion;
import es.jaimelozanodiegotorres.backapp.rest.orders.websocket.OrderMapperNotificacion;
import es.jaimelozanodiegotorres.backapp.rest.orders.websocket.OrderResponseNotificacion;
import es.jaimelozanodiegotorres.backapp.rest.products.repository.ProductRepository;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.servicios.RestaurantServicePgSqlImpl;
import es.jaimelozanodiegotorres.backapp.rest.user.service.UserServicePgSql;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static es.jaimelozanodiegotorres.backapp.rest.orders.websocket.OrderMapperNotificacion.toResponse;

@Service
@CacheConfig(cacheNames = {"orders"})
@Slf4j
public class OrderService extends CommonServiceMongo<Order, ObjectId> {
    OrderMapper mapper;
    private final ObjectMapper map;
    private final WebSocketConfig webSocketConfig;
    private WebSocketHandler webSocketService;
    private final ProductRepository productRepository;
    private final UserServicePgSql userService;
    private final RestaurantServicePgSqlImpl restaurantService;
    private final AddressesServicePgSqlImpl addressesService;


    @Autowired
    public OrderService(OrderRepository repository, ProductRepository productRepository, UserServicePgSql userService, RestaurantServicePgSqlImpl restaurantService, AddressesServicePgSqlImpl addressesService, WebSocketConfig webSocketConfig){
        super(repository);
        map = new ObjectMapper();
        this.webSocketConfig = webSocketConfig;
        this.productRepository = productRepository;
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.addressesService = addressesService;
        this.mapper = OrderMapper.INSTANCE;
        webSocketService = webSocketConfig.webSocketFunkosHandler();
    }

    public Page<Order> pageAll(Pageable pageable) {
        log.info("Listando todos los pedidos pageados");
        return repository.findAll(pageable);
    }

    @Transactional //no se si es necesario repetirlo en controller tb
    public Order save(OrderDto dto) {
        log.info("Guardando order : {}", dto);

         dto.setUserId(getLoggedUserId());//todo: commonserviceApp con los metodos

        checkOrderIds(dto);
        checkOrderedProducts(dto);
        Order order = mapper.dtoToModel(dto);
        onChange(Notificacion.Tipo.CREATE, order);
        return save(order);
    }

    public Order update(ObjectId objectId, OrderDto dto) {
        log.info("Actualizando un pedido por su id: {}", objectId.toHexString());
        if(dto.getOrderedProducts()!=null) {
            checkOrderedProducts(dto);
        }

        dto.setUserId(getLoggedUserId()); //todo: commonserviceApp con los metodos

        checkOrderIds(dto);
        var original = findById(objectId);
        isUpdatable(original);
        Order order = mapper.updateModel(original, dto);
        onChange(Notificacion.Tipo.UPDATE, order);
        return update(order);
    }

    /**
     * Busca los pedidos de un restaurante y los pagina
     * @param restaurantId id del restaurante
     * @param pageable paginacion
     * @return pedidos del restaurante
     */
    public Page<Order> findByRestaurantId(Long restaurantId, Pageable pageable) {
        log.info("Buscando los pedidos del restaurante con id: {}", restaurantId);
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
        log.info("Actualizando isPaid del pedido con id: {} a {}", objectId.toHexString(), isPaid);
        Order original = findById(objectId);
        isUpdatable(original);

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
        var list = order.getOrderedProducts();
        if(list.isEmpty()) {
            throw exceptionService.badRequestException("La lista de productos no puede ser vacia");
        }
        for(OrderedProduct orderedProduct : list){
            var repositoryProduct = productRepository.findById(orderedProduct.getProductId()).orElseThrow(()-> exceptionService.notFoundException(orderedProduct.getProductId().toString()));
            if(orderedProduct.getProductPrice()!=repositoryProduct.getPrice()) {
                throw exceptionService.badRequestException("El precio del producto con id "+orderedProduct.getProductId()+" no coincide con el de la base de datos");
            }
            if(orderedProduct.getQuantity()>repositoryProduct.getStock()) {
                throw exceptionService.badRequestException("La cantidad del producto con id " + orderedProduct.getProductId()+" es menor que la cantidad solicitada");
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

        UUID userId = order.getUserId();
        if (userId!=null){
           userService.findById(userId);
        }

        Long restaurantId = order.getRestaurantId();
        if (restaurantId!=null){
            restaurantService.findById(restaurantId);
        }

        UUID addressId = order.getAddressesId();
        if (addressId!=null){
            Addresses addresses = addressesService.findById(addressId);
            if(!addresses.getUserId().equals(userId))
                throw exceptionService.badRequestException("La direccion pasada no pertenece al usuario");
        }

    }

    public boolean deleteById(ObjectId id) throws RuntimeException {
        log.info("Borrando {} con id: {}", entityName, id);

        Order original = findById(id);
        if(!original.isDeleteable())
            throw exceptionService.badRequestException("No se puede borrar un pedido con estado " + original.getState());

        original.setState(OrderState.DELETED);
        original.setDeletedAt(LocalDateTime.now());

        repository.save(original);
        onChange(Notificacion.Tipo.DELETE, original);
        return true;
    }


    public List<Order> findByUserId(UUID id) {
        log.info("Buscando los pedidos del usuario con id: {}", id);
        return ((OrderRepository)repository).findByUserId(id);
    }

    public Order patchState(ObjectId id, OrderState state) {
        log.info("Cambiando el estado del pedido a {}", state);
        Order original = findById(id);

        isUpdatable(original);
        original.setState(state);
        // original.setUpdatedAt(LocalDateTime.now()); //todo: no deberia ser necesario

        repository.save(original);

        return original;
    }

    private void isUpdatable (Order order){
        if(!order.isUpdatable())
            throw exceptionService.badRequestException(
                    "No se puede cambiar el estado del pedido: "
                            + order.getId() + " con estado " + order.getState()
            );
    }


    public void onChange(Notificacion.Tipo tipo, Order data) {

        if (webSocketService == null) {
            webSocketService = this.webSocketConfig.webSocketFunkosHandler();
        }

        try {
            Notificacion<OrderResponseNotificacion> notificacion = new Notificacion<>(
                    "ORDERS",
                    tipo,
                    OrderMapperNotificacion.toResponse(data),
                    LocalDateTime.now().toString()
            );

            String json = map.writeValueAsString((notificacion));


            // Enviamos el mensaje a los clientes ws con un hilo, si hay muchos clientes, puede tardar
            // no bloqueamos el hilo principal que atiende las peticiones http
            Thread senderThread = new Thread(() -> {
                try {
                    webSocketService.sendMessage(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            senderThread.start();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
