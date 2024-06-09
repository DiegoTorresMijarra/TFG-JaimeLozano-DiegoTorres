package es.jaimelozanodiegotorres.backapp.rest.offers.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.jaimelozanodiegotorres.backapp.rest.offers.dto.OfferDto;
import es.jaimelozanodiegotorres.backapp.rest.offers.models.Offer;
import es.jaimelozanodiegotorres.backapp.rest.offers.service.OfferServicePgSqlImp;
import es.jaimelozanodiegotorres.backapp.rest.commons.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class OfferControllerTest {

    private final Offer offer = Offer.builder()
            .id(1L)
            .descuento(20.0)
            .fechaDesde(LocalDateTime.of(2024, 1, 1, 0, 0))
            .fechaHasta(LocalDateTime.of(2024, 12, 31, 23, 59))
            .build();

    private final OfferDto offerDto = OfferDto.builder()
            .productId(1L)
            .descuento(30.0)
            .fechaDesde(LocalDateTime.of(2024, 1, 1, 0, 0))
            .fechaHasta(LocalDateTime.of(2024, 12, 31, 23, 59))
            .build();

    private final String myEndPoint = "/offers";
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private OfferServicePgSqlImp service;

    @Autowired
    private JacksonTester<OfferDto> jsonOfferDto;

    @Autowired
    public OfferControllerTest(OfferServicePgSqlImp service) {
        this.service = service;
    }

    @Test
    void listAll() throws Exception {
        when(service.listAll()).thenReturn(Collections.singletonList(offer));

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
    void activeByProductId() throws Exception {
        when(service.findActivasByProductId(1L)).thenReturn(offer);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint + "/active/1")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("activeByProductId",
                () -> assertEquals(200, response.getStatus()),
                () -> verify(service, times(1)).findActivasByProductId(1L)
        );
    }

    @Test
    void findById() throws Exception {
        when(service.findById(1L)).thenReturn(offer);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint + "/1")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findById",
                () -> assertEquals(200, response.getStatus()),
                () -> verify(service, times(1)).findById(1L)
        );
    }

    @Test
    void saveOffer() throws Exception {
        when(service.save(any(OfferDto.class))).thenReturn(offer);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndPoint + "/saveOffer")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonOfferDto.write(offerDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("saveOffer",
                () -> assertEquals(201, response.getStatus()),
                () -> verify(service, times(1)).save(any(OfferDto.class))
        );
    }

    @Test
    void updateOffer() throws Exception {
        when(service.findById(1L)).thenReturn(offer);
        when(service.update(eq(1L), any(OfferDto.class))).thenReturn(offer);

        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndPoint + "/updateOffer/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonOfferDto.write(offerDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("updateOffer",
                () -> assertEquals(200, response.getStatus()),
                () -> verify(service, times(1)).update(eq(1L), any(OfferDto.class))
        );
    }

    @Test
    void deleteOffer() throws Exception {
        when(service.deleteById(1L)).thenReturn(true);

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndPoint + "/deleteOffer/1")
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("deleteOffer",
                () -> assertEquals(204, response.getStatus()),
                () -> verify(service, times(1)).deleteById(1L)
        );
    }

    @Test
    void deleteOffer_NotFound() throws Exception {
        doThrow(new EntityNotFoundException("1")).when(service).deleteById(1L);

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndPoint + "/deleteOffer/1")
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("deleteOffer_NotFound",
                () -> assertEquals(404, response.getStatus()),
                () -> verify(service, times(1)).deleteById(1L)
        );
    }
}
