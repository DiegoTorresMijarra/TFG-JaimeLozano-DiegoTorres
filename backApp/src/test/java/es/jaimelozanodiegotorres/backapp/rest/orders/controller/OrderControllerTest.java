package es.jaimelozanodiegotorres.backapp.rest.orders.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import es.jaimelozanodiegotorres.backapp.config.auth.SecurityConfig;
import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.auth.service.jwt.JwtServiceImpl;
import es.jaimelozanodiegotorres.backapp.rest.commons.exceptions.EntityBadRequestException;
import es.jaimelozanodiegotorres.backapp.rest.commons.exceptions.EntityNotFoundException;
import es.jaimelozanodiegotorres.backapp.rest.orders.dto.OrderDto;
import es.jaimelozanodiegotorres.backapp.rest.orders.models.Order;
import es.jaimelozanodiegotorres.backapp.rest.orders.models.OrderedProduct;
import es.jaimelozanodiegotorres.backapp.rest.orders.service.OrderService;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import es.jaimelozanodiegotorres.backapp.rest.user.models.Role;
import es.jaimelozanodiegotorres.backapp.rest.user.models.User;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "user", password = "u", roles = "USER")
@Import(SecurityConfig.class)
class OrderControllerTest {

    private final String myEndpoint="http://localhost:3000/orders";
    private final User mockUser=User.builder()
            .username("User")
            .roles(Set.of(Role.USER))
            .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
            .build();
    private final Product product= Product.builder()
            .id(1L)
            .stock(100)
            .price(10.00)
            .build();
    private final List<OrderedProduct> validList= List.of(
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
            .addressesId(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d476"))
            .orderedProducts(validList)
            .build();
    private List<Order> orderList;
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc; // Cliente MVC

    @MockBean
    private OrderService ordersService;
    @MockBean
    private final JwtServiceImpl jwtService;
    @Autowired
    public OrderControllerTest(OrderService ordersService,JwtServiceImpl jwtService) {
        this.ordersService = ordersService;
        this.jwtService = jwtService;
        mapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void setUp() {
        order1.setOrderedProducts(validList);
        orderList = List.of(order1);
    }

    @Test
    @WithAnonymousUser
    void NotAuthenticated() throws Exception {
        // Localpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint+"/listAll")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(403, response.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testFindAll() throws Exception {
        String localEndPoint=myEndpoint+"/listAll";
        when(ordersService.listAll()).thenReturn(orderList);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        List<Order> result = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertAll("findAll",
                () -> assertEquals(200, response.getStatus()),
                ()-> assertEquals(orderList,result)
        );
    }

    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testFindAllPaged() throws Exception {
        String localEndPoint=myEndpoint;
        var res= new PageImpl<>(orderList);
        when(ordersService.pageAll(any(Pageable.class))).thenReturn(res);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint + "/pageAll")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Order> result = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertAll("findAllPaged",
                () -> assertEquals(200, response.getStatus()),
                ()-> assertEquals(orderList,result.content())
        );
    }

    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testFindById() throws Exception {
        String localEndPoint=myEndpoint+"/6569a29d62996427018dc774";
        when(ordersService.findById(any(ObjectId.class))).thenReturn(order1);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Order result = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertAll("findById",
                () -> assertEquals(200, response.getStatus()),
                ()-> assertEquals(order1,result)
        );
    }

    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testFindById_ThrowsNotFoundException() throws Exception {
        String localEndPoint=myEndpoint+"/6569a29d62996427018dc774";
        when(ordersService.findById(any(ObjectId.class))).thenThrow(EntityNotFoundException.class);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll("findById_ThrowsNotFoundException",
                () -> assertEquals(404, response.getStatus())
        );
    }
    @Test
    void testSaveOrder() throws Exception {
        String localEndPoint=myEndpoint+"/saveOrder";
        when(ordersService.save(any(OrderDto.class))).thenReturn(order1);

        MockHttpServletResponse response = mockMvc.perform(
                        post(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(orderSaveDto))
                                .header("Authorization",mockUser)
                                .with(
                                        requestSpec ->{
                                            try {
                                                requestSpec.setRemoteUser(mapper.writeValueAsString(mockUser));
                                            } catch (JsonProcessingException e) {
                                                throw new RuntimeException(e);
                                            }
                                            return requestSpec;
                                        }
                                )
                )
                .andReturn().getResponse();

        Order result = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertAll("saveOrder",
                () -> assertEquals(201, response.getStatus()),
                ()-> assertEquals(order1,result)
        );
    }

    @Test
    void testSaveOrder_ShouldThrowBadRequestException() throws Exception {
        String localEndPoint=myEndpoint+"/saveOrder";
        when(ordersService.save(any(OrderDto.class))).thenThrow(EntityBadRequestException.class);
        MockHttpServletResponse response = mockMvc.perform(
                        post(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(orderSaveDto)))
                .andReturn().getResponse();
        assertAll("saveOrder_ShouldThrowBadRequestException",
                () -> assertEquals(400, response.getStatus())
        );
    }

    @Test
    @WithAnonymousUser
    void testSaveOrder_ShouldThrowForbiddenException() throws Exception {
        String localEndPoint=myEndpoint+"/saveOrder";
        MockHttpServletResponse response = mockMvc.perform(
                        post(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(orderSaveDto)))
                .andReturn().getResponse();
        assertAll("saveOrder_ShouldThrowForbiddenException",
                () -> assertEquals(403, response.getStatus())
        );
    }

    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testUpdateOrder() throws Exception {
        String localEndPoint = myEndpoint + "/updateOrder/6569a29d62996427018dc774";
        when(ordersService.update(any(ObjectId.class), any(OrderDto.class))).thenReturn(order1);
        MockHttpServletResponse response = mockMvc.perform(
                        put(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(orderSaveDto)))
                .andReturn().getResponse();

        Order result = mapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertAll("updateOrder",
                () -> assertEquals(200, response.getStatus()),
                ()-> assertEquals(order1,result)
        );
    }

    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testDeleteOrder() throws Exception {
        ObjectId orderId = new ObjectId("6569a29d62996427018dc774");
        String localEndPoint=myEndpoint+"/deleteOrder/6569a29d62996427018dc774";
        when(ordersService.deleteById(orderId)).thenReturn(true);
        MockHttpServletResponse response = mockMvc.perform(
                        delete(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll("saveOrder_ShouldThrowForbiddenException",
                () -> assertEquals(204, response.getStatus()),
                () -> verify(ordersService).deleteById(orderId)
        );
    }

    @Test
    void testDeleteOrder_Forbidden() throws Exception {
        ObjectId orderId = new ObjectId("6569a29d62996427018dc774");
        String localEndPoint=myEndpoint+"/deleteOrder/6569a29d62996427018dc774";
        MockHttpServletResponse response = mockMvc.perform(
                        delete(localEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll("saveOrder_ShouldThrowForbiddenException",
                () -> assertEquals(403, response.getStatus())
        );
    }

    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void testExistByRestaurantId() throws Exception {
        String localEndPoint = myEndpoint + "/restaurantExists/1";
        when(ordersService.existsByRestaurantId(1L)).thenReturn(true);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("testExistByRestaurantId",
                () -> assertEquals(200, response.getStatus()),
                ()-> assertTrue(Boolean.parseBoolean(response.getContentAsString()))
        );
    }

    @Test
    @WithMockUser(username = "admin", password = "u", roles = {"ADMIN","USER"})
    void findByRestaurantId() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Order> expectedPage = new PageImpl<>(orderList.subList(0,1));
        String localEndPoint=myEndpoint+"/restaurant/1";
        when(ordersService.findByRestaurantId(1L,pageable)).thenReturn(expectedPage);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll("findByRestaurantId",
                () -> assertEquals(200, response.getStatus())
        );
    }
}