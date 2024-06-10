package es.jaimelozanodiegotorres.backapp.rest.products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.commons.exceptions.EntityNotFoundException;
import es.jaimelozanodiegotorres.backapp.rest.products.dto.ProductSaveDto;
import es.jaimelozanodiegotorres.backapp.rest.products.filters.ProductFiltersDto;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import es.jaimelozanodiegotorres.backapp.rest.products.services.ProductServicePgSqlImp;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class ProductControllerTest {

    private final Product product = Product.builder()
            .id(1L)
            .name("Test Product")
            .price(10.0)
            .stock(100)
            .gluten(true)
            .build();

    private final ProductSaveDto productSaveDto = ProductSaveDto.builder()
            .name("Test Product")
            .price(10.0)
            .stock(100)
            .gluten(true)
            .categoryId(1L)
            .build();

    private final ProductFiltersDto filtersDto = ProductFiltersDto.builder().build();


    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat());
    }

    private final String myEndPoint = "/products";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ProductServicePgSqlImp service;

    @Autowired
    private JacksonTester<ProductSaveDto> jsonProductSaveDto;

    @Autowired
    public ProductControllerTest(ProductServicePgSqlImp service) {
        this.service = service;
    }

    @Test
    void listAll() throws Exception {
        List<Product> products = Arrays.asList(product);
        when(service.listAll()).thenReturn(products);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint + "/listAll")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("listAll",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(mapper.writeValueAsString(products), response.getContentAsString()),
                () -> verify(service, times(1)).listAll()
        );
    }


    @Test
    void pageAll() throws Exception {
        Page<Product> page = new PageImpl<>(Arrays.asList(product), PageRequest.of(0, 2), 2);
        PageResponse<Product> res = PageResponse.of(page, "id", "asc");
        when(service.pageAll(any())).thenReturn(res);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndPoint + "/pageAll")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("pageAll",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, page.getTotalPages()),
                () -> assertEquals(1, page.getContent().get(0).getId()),
                () -> verify(service, times(1)).pageAll(any())
        );
    }

    @Test
    void getProduct() throws Exception {
        when(service.findById(product.getId())).thenReturn(product);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint + "/" + product.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("findById",
                () -> assertEquals(200, response.getStatus()),
                () -> verify(service, times(1)).findById(product.getId())
        );
    }

    @Test
    void getProduct_NotFound() throws Exception {
        when(service.findById(product.getId()))
                .thenThrow(new EntityNotFoundException(product.getId().toString()));

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint + "/" + product.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
        verify(service, times(1)).findById(product.getId());
    }

    @Test
    void createProduct() throws Exception {
        when(service.save(productSaveDto)).thenReturn(product);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndPoint + "/saveProduct")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductSaveDto.write(productSaveDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(201, response.getStatus());
        verify(service, times(1)).save(productSaveDto);
    }

    @Test
    void updateProduct() throws Exception {
        when(service.findById(product.getId())).thenReturn(product);
        when(service.update(product.getId(), productSaveDto)).thenReturn(product);

        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndPoint + "/updateProduct" + "/" + product.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductSaveDto.write(productSaveDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
        verify(service, times(1)).update(product.getId(), productSaveDto);
    }

    @Test
    void deleteProduct() throws Exception {
        when(service.deleteById(product.getId())).thenReturn(true);

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndPoint + "/deleteProduct" + "/" + product.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll("delete",
                () -> assertEquals(204, response.getStatus())
        );
        verify(service, times(1)).deleteById(product.getId());
    }

    @Test
    void deleteProduct_NotFound() throws Exception {
        doThrow(new EntityNotFoundException(product.getId().toString())).when(service).deleteById(product.getId());

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndPoint + "/deleteProduct" + "/" + product.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(service, times(1)).deleteById(product.getId());
    }

    @Test
    void patchImage() throws Exception {
        byte[] imageBytes = "fakeImageContent".getBytes();
        MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpeg", imageBytes);

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.multipart(HttpMethod.PATCH,myEndPoint + "/updateProductPhoto/" + product.getId())
                                .file(imageFile)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
        verify(service, times(1)).updateProductPhoto(eq(product.getId()), any(MultipartFile.class));
    }
}
