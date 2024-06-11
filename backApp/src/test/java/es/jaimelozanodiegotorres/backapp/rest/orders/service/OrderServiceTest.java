package es.jaimelozanodiegotorres.backapp.rest.orders.service;

import es.jaimelozanodiegotorres.backapp.config.websocket.WebSocketConfig;
import es.jaimelozanodiegotorres.backapp.config.websocket.WebSocketHandler;
import es.jaimelozanodiegotorres.backapp.rest.addresses.models.Addresses;
import es.jaimelozanodiegotorres.backapp.rest.addresses.services.AddressesServicePgSqlImpl;
import es.jaimelozanodiegotorres.backapp.rest.commons.exceptions.EntityNotFoundException;
import es.jaimelozanodiegotorres.backapp.rest.orders.dto.OrderDto;
import es.jaimelozanodiegotorres.backapp.rest.orders.models.Order;
import es.jaimelozanodiegotorres.backapp.rest.orders.models.OrderState;
import es.jaimelozanodiegotorres.backapp.rest.orders.models.OrderedProduct;
import es.jaimelozanodiegotorres.backapp.rest.orders.repository.OrderRepository;
import es.jaimelozanodiegotorres.backapp.rest.orders.websocket.Notificacion;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import es.jaimelozanodiegotorres.backapp.rest.products.repository.ProductRepository;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.modelos.Restaurant;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.servicios.RestaurantServicePgSqlImpl;
import es.jaimelozanodiegotorres.backapp.rest.user.models.Role;
import es.jaimelozanodiegotorres.backapp.rest.user.models.User;
import es.jaimelozanodiegotorres.backapp.rest.user.service.UserServicePgSql;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    WebSocketHandler webSocketHandlerMock = mock(WebSocketHandler.class);
    @Mock
    private OrderRepository ordersCrudRepository;

    @Mock
    private UserServicePgSql clientsRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RestaurantServicePgSqlImpl restaurantRepository;

    @Mock
    private AddressesServicePgSqlImpl addressesService;

    @Mock
    private WebSocketConfig webSocketConfig;

    @InjectMocks
    private OrderService ordersService;

    private User user = User.builder()
            .id(UUID.randomUUID())
            .roles(Set.of(Role.USER, Role.ADMIN,Role.WORKER))
            .build();

    private final Product product= Product.builder()
            .id(1L)
            .stock(100)
            .price(10.00)
            .build();

    private final List<OrderedProduct> validList=List.of(
            OrderedProduct.builder()
                    .quantity(10)
                    .productId(1L)
                    .productPrice(10.00)
                    .totalPrice(100.00)
                    .build(),
            OrderedProduct.builder()
                    .quantity(50)
                    .productId(1L)
                    .productPrice(10.00)
                    .totalPrice(500.00)
                    .build()
    );
    private final List<OrderedProduct> invalidList= List.of(
            OrderedProduct.builder()
                    .quantity(10)
                    .productId(2L)
                    .productPrice(0.00)
                    .totalPrice(100.00)
                    .build(),
            OrderedProduct.builder()
                    .quantity(500)
                    .productId(1L)
                    .productPrice(10.00)
                    .totalPrice(500.00)
                    .build()
    );

    private final Order order1= Order.builder()
            .id(new ObjectId())
            .userId(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"))
            .restaurantId(1L)
            .addressesId(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d476"))
            .orderedProducts(validList)
            .build();
    private final Order order2= Order.builder()
            .id(new ObjectId())
            .build();

    private final OrderDto orderSaveDto= OrderDto.builder()
            .userId(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"))
            .restaurantId(1L)
            .orderedProducts(validList)
            .build();

    private List<Order> orderList;

    @BeforeEach
    void setUp2(){
        order1.setOrderedProducts(validList);
        orderList = List.of(order1,order2);
        ordersService.setWebSocketService(webSocketHandlerMock);
    }

    @BeforeEach
    void setUp() {
        // Crea un mock de SecurityContext y Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        user = User.builder()
                .id(user.getId())
                .roles(Set.of(Role.USER, Role.ADMIN,Role.WORKER))
                .build();

        // Configura el SecurityContext para devolver el Authentication mock
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        // Configura el Authentication para devolver el usuario logueado mock
        lenient().when(authentication.getPrincipal()).thenReturn(user);
        // Establece el SecurityContextHolder con el SecurityContext mock
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testFindAllPageable_ShouldReturnAll_whenNoParamsPassed() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Order> expectedPage = new PageImpl<>(orderList);
        when(ordersCrudRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Order> result = ordersService.pageAll(pageable);

        verify(ordersCrudRepository).findAll(pageable);

        assertAll("testFindAllPageable_ShouldReturnAll_whenNoParamsPassed",
                () -> assertNotNull(result),
                ()-> assertFalse(result.isEmpty()),
                () -> assertEquals(2, result.getTotalElements())
        );
    }

    @Test
    void testFindAllPageable_ShouldReturnCorrect_whenParamsPassed() {
        // Mock data
        Pageable pageable = PageRequest.of(0, 1, Sort.by("id").ascending());
        Page<Order> expectedPage = new PageImpl<>(orderList.subList(0, 1));

        when(ordersCrudRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Order> result = ordersService.pageAll(pageable);

        assertAll("testFindAllPageable_ShouldReturnCorrect_whenParamsPassed",
                ()-> assertNotNull(result),
                ()-> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements())
        );
        verify(ordersCrudRepository).findAll(pageable);
    }

    @Test
    void testFindById_ShouldReturnCorrect_whenParamsPassed() {
        ObjectId orderId = new ObjectId(order1.getId());
        when(ordersCrudRepository.findByIdAndDeletedAtIsNull(any(ObjectId.class))).thenReturn(Optional.of(order1));

        Order result = ordersService.findById(orderId);

        assertAll("testFindById_ShouldReturnCorrect_whenParamsPassed",
                () -> assertNotNull(result),
                () -> assertEquals(order1, result)
        );
        verify(ordersCrudRepository).findByIdAndDeletedAtIsNull(orderId);
    }

    @Test
    void testFindById_ShouldThrowException_whenIncorrectParamsPassed() {
        ObjectId orderId = new ObjectId();
        when(ordersCrudRepository.findByIdAndDeletedAtIsNull(any(ObjectId.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ordersService.findById(orderId));
        verify(ordersCrudRepository).findByIdAndDeletedAtIsNull(orderId);
    }

    @Test
    void testSave_ShouldReturnCorrect() {
        // Arrange
        UUID userId = user.getId();
        Long restaurantId = 1L;
        UUID addressId = UUID.randomUUID();
        Long productId = 1L;

        Restaurant restaurant = new Restaurant();
        Addresses address = mock(Addresses.class);
        Product product = new Product();
        product.setId(productId);
        product.setPrice(100.0);
        product.setStock(10);

        // OrderedProduct setup
        OrderedProduct orderedProduct = new OrderedProduct();
        orderedProduct.setProductId(productId);
        orderedProduct.setProductPrice(100.0);
        orderedProduct.setQuantity(2);

        orderSaveDto.setUserId(userId);
        orderSaveDto.setRestaurantId(restaurantId);
        orderSaveDto.setAddressesId(addressId);
        orderSaveDto.setOrderedProducts(List.of(orderedProduct));

        // Mock the repository and service methods
        when(address.getUserId()).thenReturn(user.getId());
        when(clientsRepository.findById(any(UUID.class))).thenReturn(user);
        when(restaurantRepository.findById(any(Long.class))).thenReturn(restaurant);
        when(addressesService.findById(any(UUID.class))).thenReturn(address);
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        when(ordersCrudRepository.save(any(Order.class))).thenReturn(order1);

        // Act
        Order result = ordersService.save(orderSaveDto);

        // Assert
        assertAll("testSave_ShouldReturnCorrect",
                () -> assertNotNull(result),
                () -> assertEquals(order1, result)
        );

        // Verify the interactions
        verify(clientsRepository).findById(userId);
        verify(restaurantRepository).findById(restaurantId);
        verify(addressesService).findById(addressId);
        verify(productRepository).findById(productId);
        verify(productRepository).save(product);
        verify(ordersCrudRepository).save(any(Order.class));
    }

    @Test
    void testDeleteById() {
        ObjectId orderId = new ObjectId();
        Order order = mock(Order.class);
        order.setId(orderId);
        order.setState(OrderState.ACCEPTED);
        when(order.isDeleteable()).thenReturn(true);
        when(order.getRestaurantId()).thenReturn(1L);
        when(order.getUserId()).thenReturn(user.getId());
        when(order.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(order.getUpdatedAt()).thenReturn(LocalDateTime.now());
        when(order.getAddressesId()).thenReturn(UUID.randomUUID());
        when(ordersCrudRepository.findByIdAndDeletedAtIsNull(orderId)).thenReturn(Optional.of(order));

        ordersService.deleteById(orderId);

        verify(ordersCrudRepository).findByIdAndDeletedAtIsNull(orderId);
    }

    @Test
    void findByRestaurantId() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Order> expectedPage = new PageImpl<>(orderList.subList(0,1));
        when(ordersCrudRepository.findByRestaurantId(1L,pageable)).thenReturn(expectedPage);

        Page<Order> result = ordersService.findByRestaurantId(1L,pageable);

        assertAll("findByRestaurantId",
                () -> assertNotNull(result),
                ()-> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements())
        );
        verify(ordersCrudRepository).findByRestaurantId(1L,pageable);
    }

    @Test
    void existsByRestaurantId() {
        when(ordersCrudRepository.existsByRestaurantId(1L)).thenReturn(true);

        Boolean result = ordersService.existsByRestaurantId(1L);

        assertAll("existsByRestaurantId_ShouldReturnTrue",
                () -> assertNotNull(result),
                ()-> assertTrue(result)
        );
        verify(ordersCrudRepository).existsByRestaurantId(1L);
    }

    @Test
    void testUpdateIsPaidById() {
        ObjectId orderId = new ObjectId(order1.getId());
        Order updatedIsPaid= Order.builder()
                .id(orderId)
                .isPaid(true)
                .build();
        when(ordersCrudRepository.findByIdAndDeletedAtIsNull(orderId)).thenReturn(Optional.of(order1));
        when(ordersCrudRepository.save(any(Order.class))).thenReturn(updatedIsPaid);

        Order result = ordersService.updateIsPaidById(orderId, true);

        assertAll("testUpdateIsPaidById",
                () -> assertNotNull(result),
                () -> assertTrue(result.getIsPaid()),
                ()-> assertEquals(updatedIsPaid.getId(), result.getId())
        );
        verify(ordersCrudRepository).findByIdAndDeletedAtIsNull(orderId);
        verify(ordersCrudRepository).save(any(Order.class));
    }

    @Test
    void findByUserId() {
        List<Order> expectedPage = List.of();
        when(ordersCrudRepository.findByUserId(user.getId())).thenReturn(expectedPage);

        List<Order> result = ordersService.findByUserId(user.getId());

        assertAll("findByRestaurantId",
                () -> assertNotNull(result),
                ()-> assertTrue(result.isEmpty())
        );
        verify(ordersCrudRepository).findByUserId(user.getId());
    }

    @Test
    void onChange() throws IOException {
        doNothing().when(webSocketHandlerMock).sendMessage(any(String.class));
        ordersService.onChange(Notificacion.Tipo.CREATE, order1);
    }

}