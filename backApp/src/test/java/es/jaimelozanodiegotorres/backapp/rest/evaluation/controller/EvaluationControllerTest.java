package es.jaimelozanodiegotorres.backapp.rest.evaluation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.jaimelozanodiegotorres.backapp.rest.commons.exceptions.EntityNotFoundException;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationResponseDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.models.Evaluation;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.services.EvaluationServicePgSqlImp;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.dto.RestaurantDto;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.servicios.RestaurantServicePgSqlImpl;
import es.jaimelozanodiegotorres.backapp.rest.user.models.Role;
import es.jaimelozanodiegotorres.backapp.rest.user.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class EvaluationControllerTest {
    private User user = User.builder()
            .id(UUID.randomUUID())
            .roles(Set.of(Role.USER, Role.ADMIN,Role.WORKER))
            .build();
    private final Evaluation res1 = Evaluation.builder()
            .id(1L)
            .value(4)
            .comment("Bueno")
            .deletedAt(null)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .product(new Product())
            .user(user)
            .build();

    private final Evaluation res2 = Evaluation.builder()
            .id(2L)
            .value(2)
            .comment("Malo")
            .deletedAt(null)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .product(new Product())
            .user(user)
            .build();

    private final EvaluationDto newDto = EvaluationDto.builder()
            .value(2)
            .comment("Malo")
            .productId(1L)
            .build();

    private final EvaluationResponseDto newResponseDto = EvaluationResponseDto.builder()
            .value(2)
            .comment("Malo")
            .productId(1L)
            .userName(user.getUsername())
            .createdAt(LocalDateTime.now())
            .build();

    private final String myEndPoint = "http://localhost:3000/evaluations";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private EvaluationServicePgSqlImp service;
    @Autowired
    private JacksonTester<EvaluationDto> jsonEvaluationDto;

    @Autowired
    public EvaluationControllerTest(EvaluationServicePgSqlImp service) {
        this.service = service;
        //mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void listAll() throws Exception {
        when(service.listAll()).thenReturn(List.of(res1,res2));

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint + "/listAll")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("listAll",
                () -> assertEquals(200, response.getStatus()),
                () -> verify(service, times(1)).listAll()
        );
    }
    @Test
    void listAllByProductId() throws Exception {
        when(service.findByProductId(1L)).thenReturn(List.of(newResponseDto));

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint + "/listAll/" + 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("listAllByProductId",
                () -> assertEquals(200, response.getStatus()),
                () -> verify(service, times(1)).findByProductId(1L)
        );
    }

    @Test
    void getEvaluation() throws Exception {
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
    void getEvaluation_NotFound() throws Exception {
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
    void createEvaluation() throws Exception {

        when(service.save(newDto)).thenReturn(res1);
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndPoint + "/saveEvaluation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonEvaluationDto.write(newDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(201, response.getStatus());
        verify(service, times(1)).save(newDto);
    }

    @Test
    void updateEvaluation() throws Exception {
        when(service.findById(res1.getId())).thenReturn(res1);
        when(service.update(res1.getId(), newDto)).thenReturn(res1);
        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndPoint + "/updateEvaluation" + "/" + res1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonEvaluationDto.write(newDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
        verify(service, times(1)).update(res1.getId(), newDto);
    }

    @Test
    void deleteEvaluation() throws Exception {
        when(service.deleteById(res1.getId())).thenReturn(true);
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndPoint + "/deleteEvaluation" + "/"+res1.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("delete",
                () -> assertEquals(204, response.getStatus())
        );
        verify(service,times(1)).deleteById(res1.getId());
    }

    @Test
    void deleteEvaluation_NotFound() throws Exception {
        doThrow(new EntityNotFoundException(res1.getId().toString())).when(service).deleteById(res1.getId());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndPoint + "/deleteEvaluation" + "/"+res1.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(service, times(1)).deleteById(res1.getId());
    }
}