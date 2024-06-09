package es.jaimelozanodiegotorres.backapp.rest.evaluation.services;

import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationResponseDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.mappers.EvaluationResponseMapper;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.mappers.EvaluationSaveMapper;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.models.Evaluation;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.repository.EvaluationRepository;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import es.jaimelozanodiegotorres.backapp.rest.products.services.ProductServicePgSqlImp;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.modelos.Restaurant;
import es.jaimelozanodiegotorres.backapp.rest.user.models.Role;
import es.jaimelozanodiegotorres.backapp.rest.user.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluationServicePgSqlImpTest {
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


    @Mock
    private ProductServicePgSqlImp serviceProduct;
    @Mock
    private EvaluationRepository repository;
    private final EvaluationSaveMapper mapper = EvaluationSaveMapper.INSTANCE;
    private final EvaluationResponseMapper mapper2 = EvaluationResponseMapper.INSTANCE;
    @InjectMocks
    EvaluationServicePgSqlImp service;

    @BeforeEach
    void setUp() {
        // Crea un mock de SecurityContext y Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        user = User.builder()
                .id(user.getId())
                .roles(Set.of(Role.USER, Role.ADMIN,Role.WORKER))
                .build();

        // Configura el SecurityContext para devolver el Authentication mock
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        // Configura el Authentication para devolver el usuario logueado mock
        lenient().when(authentication.getPrincipal()).thenReturn(user);
        // Establece el SecurityContextHolder con el SecurityContext mock
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void save() {

        Product product = new Product();
        when(serviceProduct.findById(newDto.getProductId())).thenReturn(product);
        Evaluation newRes = mapper.dtoToModel(newDto);
        newRes.setProduct(product);
        when(repository.save(any(Evaluation.class))).thenReturn(newRes);

        Evaluation result = service.save(newDto);

        assertAll("Save",
                () -> assertNotNull(result),
                () -> assertEquals(newRes.getId(), result.getId()),
                () -> verify(serviceProduct, times(1)).findById(newDto.getProductId()),
                () -> verify(repository, times(1)).save(any(Evaluation.class))
        );

    }

    @Test
    void update() {
        when(repository.findByIdAndDeletedAtIsNull(res1.getId())).thenReturn(Optional.of(res1));
        Evaluation updatedRestau = mapper.updateModel(res1, newDto);
        when(repository.save(updatedRestau)).thenReturn(updatedRestau);

        Evaluation result = service.update(res1.getId(), newDto);

        assertAll("Update",
                () -> assertNotNull(result),
                () -> assertEquals(res1.getId(), result.getId()),
                () -> verify(repository, times(1)).findByIdAndDeletedAtIsNull(res1.getId()),
                () -> verify(repository, times(1)).save(updatedRestau)
        );
    }

    @Test
    void testFindByProductId() {
        List<Evaluation> evaluations = Arrays.asList(res1, res2);
        when(repository.findByProductId(1L)).thenReturn(evaluations);

        List<EvaluationResponseDto> result = service.findByProductId(1L);

        assertAll("findByProductId",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> verify(repository, times(1)).findByProductId(1L)
        );
    }
}