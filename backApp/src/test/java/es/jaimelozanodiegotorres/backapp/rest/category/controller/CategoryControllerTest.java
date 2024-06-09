package es.jaimelozanodiegotorres.backapp.rest.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.jaimelozanodiegotorres.backapp.rest.category.dto.CategoryDto;
import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;
import es.jaimelozanodiegotorres.backapp.rest.category.services.CategoryServicePgSqlImp;
import es.jaimelozanodiegotorres.backapp.rest.commons.exceptions.EntityNotFoundException;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.dto.RestaurantDto;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class CategoryControllerTest {
    private List<Product> products;
    private final Category cat1 = Category.builder()
            .id(1L)
            .name("categoria 1")
            .deletedAt(null)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .products(products)
            .build();

    private final Category cat2 = Category.builder()
            .id(1L)
            .name("categoria 2")
            .deletedAt(null)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .products(products)
            .build();

    private final CategoryDto newDto = CategoryDto.builder()
            .name("newDto")
            .build();

    private final String myEndPoint = "http://localhost:3000/categories";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private CategoryServicePgSqlImp service;
    @Autowired
    private JacksonTester<CategoryDto> jsonCategoryDto;

    @Autowired
    public CategoryControllerTest(CategoryServicePgSqlImp service) {
        this.service = service;
        //mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getCategory() throws Exception {
        when(service.findById(cat1.getId())).thenReturn(cat1);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint + "/" + cat1.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findById",
                () -> assertEquals(200, response.getStatus()),
                () -> verify(service, times(1)).findById(cat1.getId())
        );
    }

    @Test
    void getCategory_NotFound() throws Exception {
        when(service.findById(cat1.getId()))
                .thenThrow(new EntityNotFoundException(cat1.getId().toString()));

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint + "/" + cat1.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
        verify(service, times(1)).findById(cat1.getId());
    }

    @Test
    void findByName() throws Exception {
        when(service.findByName(cat1.getName())).thenReturn(cat1);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint + "/nombre/" + cat1.getName())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findByName",
                () -> assertEquals(200, response.getStatus()),
                () -> verify(service, times(1)).findByName(cat1.getName())
        );
    }

    @Test
    void getByName_NotFound() throws Exception {
        when(service.findByName(cat1.getName()))
                .thenThrow(new EntityNotFoundException(cat1.getName()));

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint + "/nombre/" + cat1.getName())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
        verify(service, times(1)).findByName(cat1.getName());
    }

    @Test
    void createCategory() throws Exception {
        when(service.save(newDto)).thenReturn(cat1);
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndPoint + "/saveCategory")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonCategoryDto.write(newDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(201, response.getStatus());
        verify(service, times(1)).save(newDto);
    }

    @Test
    void updateCategory() throws Exception {
        when(service.findById(cat1.getId())).thenReturn(cat1);
        when(service.update(cat1.getId(), newDto)).thenReturn(cat1);
        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndPoint + "/updateCategory" + "/" + cat1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonCategoryDto.write(newDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
        verify(service, times(1)).update(cat1.getId(), newDto);
    }

    @Test
    void deleteCategory() throws Exception {
        when(service.deleteById(cat1.getId())).thenReturn(true);
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndPoint + "/deleteCategory" + "/"+ cat1.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("delete",
                () -> assertEquals(204, response.getStatus())
        );
        verify(service,times(1)).deleteById(cat1.getId());
    }

    @Test
    void deleteCategory_NotFound() throws Exception {
        doThrow(new EntityNotFoundException(cat1.getId().toString())).when(service).deleteById(cat1.getId());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndPoint + "/deleteCategory" + "/"+ cat1.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(service, times(1)).deleteById(cat1.getId());
    }
}