package es.jaimelozanodiegotorres.backapp.rest.restaurants.servicios;

import es.jaimelozanodiegotorres.backapp.rest.commons.repository.CommonRepository;
import es.jaimelozanodiegotorres.backapp.rest.commons.services.CommonServicePgSql;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.dto.RestaurantDto;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.mapper.RestaurantMapper;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.modelos.Restaurant;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.repositories.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServicePgSqlImplTest {

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

    @Mock
    private RestaurantRepository repository;
    private final RestaurantMapper mapper = RestaurantMapper.INSTANCE;
    @InjectMocks
    RestaurantServicePgSqlImpl service;

    @Test
    void findAll_ShouldReturnAllRestaurantWithoutParameters() {
        List<Restaurant> expectedRestau = Arrays.asList(res1, res2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("cod").ascending());
        Page<Restaurant> expectedPage = new PageImpl<>(expectedRestau);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Restaurant> actualPage = service.pageAll(Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        assertAll("pageAll",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_ShouldReturnRestaurantsByName_WhenNameParameterProvided() {
        Optional<String> name = Optional.of("Restaurante 1");
        List<Restaurant> expectedRestau = List.of(res1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("cod").ascending());
        Page<Restaurant> expectedPage = new PageImpl<>(expectedRestau);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Restaurant> actualPage = service.pageAll(name, Optional.empty(), Optional.empty(),  pageable);
        assertAll("findAllWithName",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void save() {
        Restaurant newRes = mapper.dtoToModel(newDto);
        when(repository.save(any(Restaurant.class))).thenReturn(newRes);

        Restaurant result = service.save(newDto);

        assertAll("Save",
                () -> assertNotNull(result),
                () -> assertEquals(newRes.getId(), result.getId()),
                () -> verify(repository, times(1)).save(any(Restaurant.class))
        );
    }

    @Test
    void update() {
        when(repository.findById(res1.getId())).thenReturn(Optional.of(res1));
        Restaurant updatedRestau = mapper.updateModel(res1, newDto);
        when(repository.save(updatedRestau)).thenReturn(updatedRestau);
        Restaurant result = service.update(res1.getId(), newDto);
        assertAll("Update",
                () -> assertNotNull(result),
                () -> assertEquals(res1.getId(), result.getId()),
                () -> verify(repository, times(1)).findById(res1.getId()),
                () -> verify(repository, times(1)).save(updatedRestau)
        );
    }
}