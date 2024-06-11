package es.jaimelozanodiegotorres.backapp.rest.addresses.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.jaimelozanodiegotorres.backapp.rest.addresses.dto.AddressSaveDto;
import es.jaimelozanodiegotorres.backapp.rest.addresses.models.Addresses;
import es.jaimelozanodiegotorres.backapp.rest.addresses.services.AddressesServicePgSqlImpl;
import es.jaimelozanodiegotorres.backapp.rest.commons.exceptions.EntityNotFoundException;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class AddressesControllerTest {

    private final Addresses address1 = Addresses.builder()
            .id(UUID.randomUUID())
            .country("España")
            .province("Madrid")
            .city("Leganés")
            .street("Rioja")
            .number("101")
            .apartment("Bajo C")
            .postalCode("28915")
            .extraInfo("Cuidado con el Perro")
            .name("Casa de la familia torres")
            .userId(UUID.randomUUID())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    private final AddressSaveDto newDto = AddressSaveDto.builder()
            .country("España")
            .province("Madrid")
            .city("Leganés")
            .street("Rioja")
            .number("101")
            .apartment("Bajo C")
            .postalCode("28915")
            .extraInfo("Cuidado con el Perro")
            .name("Casa de la familia torres")
            .build();

    private final String myEndPoint = "http://localhost:3000/addresses";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private AddressesServicePgSqlImpl service;
    @Autowired
    private JacksonTester<AddressSaveDto> jsonAddressSaveDto;

    @Autowired
    public AddressesControllerTest(AddressesServicePgSqlImpl service) {
        this.service = service;
    }

    @Test
    void getAddress() throws Exception {
        when(service.findById(address1.getId())).thenReturn(address1);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint + "/" + address1.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findById",
                () -> assertEquals(200, response.getStatus()),
                () -> verify(service, times(1)).findById(address1.getId())
        );
    }

    @Test
    void getAddress_NotFound() throws Exception {
        when(service.findById(address1.getId()))
                .thenThrow(new EntityNotFoundException(address1.getId().toString()));

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint + "/" + address1.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
        verify(service, times(1)).findById(address1.getId());
    }

    @Test
    void createAddress() throws Exception {
        when(service.save(newDto)).thenReturn(address1);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndPoint + "/saveAddress")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonAddressSaveDto.write(newDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(201, response.getStatus());
        verify(service, times(1)).save(newDto);
    }

    @Test
    void updateAddress() throws Exception {
        when(service.findById(address1.getId())).thenReturn(address1);
        when(service.update(address1.getId(), newDto)).thenReturn(address1);
        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndPoint + "/updateAddress" + "/" + address1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonAddressSaveDto.write(newDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
        verify(service, times(1)).update(address1.getId(), newDto);
    }

    @Test
    void deleteAddress() throws Exception {
        when(service.deleteById(address1.getId())).thenReturn(true);
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndPoint + "/deleteAddress" + "/" + address1.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("delete",
                () -> assertEquals(204, response.getStatus())
        );
        verify(service,times(1)).deleteById(address1.getId());
    }

    @Test
    void deleteAddress_NotFound() throws Exception {
        doThrow(new EntityNotFoundException(address1.getId().toString())).when(service).deleteById(address1.getId());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndPoint + "/deleteAddress" + "/" + address1.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(service, times(1)).deleteById(address1.getId());
    }
}
