package es.jaimelozanodiegotorres.backapp.rest.products.services;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;
import es.jaimelozanodiegotorres.backapp.rest.category.services.CategoryServicePgSqlImp;
import es.jaimelozanodiegotorres.backapp.rest.products.dto.ProductSaveDto;
import es.jaimelozanodiegotorres.backapp.rest.products.mapper.ProductMapper;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import es.jaimelozanodiegotorres.backapp.rest.products.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServicePgSqlImpTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryServicePgSqlImp categoryService;

    @InjectMocks
    private ProductServicePgSqlImp productService;

    private final ProductMapper productMapper = ProductMapper.INSTANCE;

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

    Category category = Category.SIN_CATEGORIA;

    @Test
    void save() {
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(categoryService.findById(anyLong())).thenReturn(category);

        Product result = productService.save(productSaveDto);

        assertAll("Save",
                () -> assertNotNull(result),
                () -> assertEquals(product.getId(), result.getId()),
                () -> verify(productRepository, times(1)).save(any(Product.class))
        );
    }

    @Test
    void update() {
        when(productRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(categoryService.findById(anyLong())).thenReturn(category);

        Product result = productService.update(product.getId(), productSaveDto);

        assertAll("Update",
                () -> assertNotNull(result),
                () -> assertEquals(product.getId(), result.getId()),
                () -> verify(productRepository, times(1)).findByIdAndDeletedAtIsNull(anyLong()),
                () -> verify(productRepository, times(1)).save(any(Product.class))
        );
    }

    @Test
    void updateProductPhoto() throws IOException {
        MultipartFile image = mock(MultipartFile.class);
        when(image.getContentType()).thenReturn("image/jpeg");
        when(image.getInputStream()).thenReturn(mock(InputStream.class));
        when(productRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.updateProductPhoto(product.getId(), image);

        assertAll("Update Product Photo",
                () -> assertNotNull(result),
                () -> assertEquals(product.getId(), result.getId()),
                () -> verify(productRepository, times(1)).save(any(Product.class))
        );
    }

    @Test
    void pageAll() {
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by("id").ascending());
        Page<Product> page = new PageImpl<>(Collections.singletonList(product), pageRequest, 1);
        when(productRepository.findAll(any(Specification.class), eq(pageRequest))).thenReturn(page);

        PageResponse<Product> result = productService.pageAll(null);

        assertAll("Page All",
                () -> assertNotNull(result),
                () -> assertFalse(result.empty()),
                () -> assertEquals(1, result.totalPages()),
                () -> verify(productRepository, times(1)).findAll(any(Specification.class), eq(pageRequest))
        );
    }

    @Test
    void findById() {
        when(productRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.of(product));

        Product result = productService.findById(product.getId());

        assertAll("FindById",
                () -> assertNotNull(result),
                () -> assertEquals(product.getId(), result.getId()),
                () -> verify(productRepository, times(1)).findByIdAndDeletedAtIsNull(anyLong())
        );
    }
}
