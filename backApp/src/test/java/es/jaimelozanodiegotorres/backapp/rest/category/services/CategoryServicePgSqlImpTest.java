package es.jaimelozanodiegotorres.backapp.rest.category.services;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.category.dto.CategoryDto;
import es.jaimelozanodiegotorres.backapp.rest.category.filters.CategoryFilters;
import es.jaimelozanodiegotorres.backapp.rest.category.mappers.CategoryMapper;
import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;
import es.jaimelozanodiegotorres.backapp.rest.category.repository.CategoryRepository;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.dto.RestaurantDto;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.mapper.RestaurantMapper;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.modelos.Restaurant;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.repositories.RestaurantRepository;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.servicios.RestaurantServicePgSqlImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServicePgSqlImpTest {
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

    @Mock
    private CategoryRepository repository;
    private final CategoryMapper mapper = CategoryMapper.INSTANCE;
    @InjectMocks
    CategoryServicePgSqlImp service;

    @Test
    void save() {
        Category newRes = mapper.dtoToModel(newDto);
        when(repository.save(any(Category.class))).thenReturn(newRes);

        Category result = service.save(newDto);

        assertAll("Save",
                () -> assertNotNull(result),
                () -> assertEquals(newRes.getId(), result.getId()),
                () -> verify(repository, times(1)).save(any(Category.class))
        );
    }


    @Test
    void findByName() {
        when(repository.findByName("Nombre")).thenReturn(Optional.of(cat1));
        Category result = service.findByName("Nombre");

        assertAll("FindByName",
                () -> assertNotNull(result),
                () -> verify(repository, times(1)).findByName("Nombre")
        );
    }
    @Test
    void update() {
        when(repository.findByIdAndDeletedAtIsNull(cat1.getId())).thenReturn(Optional.of(cat1));
        Category updatedRestau = mapper.updateModel(cat1, newDto);
        when(repository.save(updatedRestau)).thenReturn(updatedRestau);

        Category result = service.update(cat1.getId(), newDto);

        assertAll("Update",
                () -> assertNotNull(result),
                () -> assertEquals(cat1.getId(), result.getId()),
                () -> verify(repository, times(1)).findByIdAndDeletedAtIsNull(cat1.getId()),
                () -> verify(repository, times(1)).save(updatedRestau)
        );
    }
}