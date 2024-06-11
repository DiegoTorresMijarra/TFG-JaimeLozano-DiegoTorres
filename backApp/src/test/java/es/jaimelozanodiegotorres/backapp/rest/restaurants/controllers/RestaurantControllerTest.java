package es.jaimelozanodiegotorres.backapp.rest.restaurants.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.commons.exceptions.EntityNotFoundException;
import es.jaimelozanodiegotorres.backapp.rest.commons.exceptions.ExceptionService;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.dto.RestaurantDto;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.modelos.Restaurant;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.servicios.RestaurantServicePgSqlImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class RestaurantControllerTest {

    private final Restaurant res1 = Restaurant.builder()
            .id(1L)
            .name("Restaurante 1")
            .phone(String.valueOf(123456789))
            .address("address")
            .deletedAt(null)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    private final Restaurant res2 = Restaurant.builder()
            .id(2L)
            .name("Restaurante 2")
            .phone(String.valueOf(123456789))
            .address("address2")
            .deletedAt(null)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    private final RestaurantDto newDto = RestaurantDto.builder()
            .name("newDto")
            .phone(String.valueOf(123456719))
            .address("addressDto")
            .build();

    private final String myEndPoint = "http://localhost:3000/restaurants";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private RestaurantServicePgSqlImpl service;
    @Autowired
    private JacksonTester<RestaurantDto> jsonRestaurantDto;

    @Autowired
    public RestaurantControllerTest(RestaurantServicePgSqlImpl service) {
        this.service = service;
        //mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getRestaurant() throws Exception {
        when(service.findById(res1.getId())).thenReturn(res1);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint + "/" + res1.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findById",
                () -> assertEquals(200, response.getStatus()),
                () -> verify(service, times(1)).findById(res1.getId())
        );
    }

    @Test
    void getRestaurant_NotFound() throws Exception {
        when(service.findById(res1.getId()))
                .thenThrow(new EntityNotFoundException(res1.getId().toString()));

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint + "/" + res1.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
        verify(service, times(1)).findById(res1.getId());
    }

    @Test
    void createRestaurant() throws Exception {

        when(service.save(newDto)).thenReturn(res1);
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndPoint + "/saveRestaurant")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRestaurantDto.write(newDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(201, response.getStatus());
        verify(service, times(1)).save(newDto);
    }

    @Test
    void updateRestaurant() throws Exception {
        when(service.findById(res1.getId())).thenReturn(res1);
        when(service.update(res1.getId(), newDto)).thenReturn(res1);
        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndPoint + "/updateRestaurant" + "/" + res1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRestaurantDto.write(newDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
        verify(service, times(1)).update(res1.getId(), newDto);
    }

    @Test
    void deleteRestaurant() throws Exception {
        when(service.deleteById(res1.getId())).thenReturn(true);
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndPoint + "/deleteRestaurant" + "/"+res1.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("delete",
                () -> assertEquals(204, response.getStatus())
        );
        verify(service,times(1)).deleteById(res1.getId());
    }

    @Test
    void deleteRestaurant_NotFound() throws Exception {
        doThrow(new EntityNotFoundException(res1.getId().toString())).when(service).deleteById(res1.getId());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndPoint + "/deleteRestaurant" + "/"+res1.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(service, times(1)).deleteById(res1.getId());
    }
}