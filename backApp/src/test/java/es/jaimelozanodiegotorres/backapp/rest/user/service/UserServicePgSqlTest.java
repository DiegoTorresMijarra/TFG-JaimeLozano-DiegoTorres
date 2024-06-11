package es.jaimelozanodiegotorres.backapp.rest.user.service;

import es.jaimelozanodiegotorres.backapp.rest.addresses.models.Addresses;
import es.jaimelozanodiegotorres.backapp.rest.addresses.repository.AddressesRepository;
import es.jaimelozanodiegotorres.backapp.rest.addresses.services.AddressesServicePgSqlImpl;
import es.jaimelozanodiegotorres.backapp.rest.orders.models.Order;
import es.jaimelozanodiegotorres.backapp.rest.orders.repository.OrderRepository;
import es.jaimelozanodiegotorres.backapp.rest.user.dto.UserDto;
import es.jaimelozanodiegotorres.backapp.rest.user.dto.UserResponseDto;
import es.jaimelozanodiegotorres.backapp.rest.user.mapper.UserSaveMapper;
import es.jaimelozanodiegotorres.backapp.rest.user.models.Role;
import es.jaimelozanodiegotorres.backapp.rest.user.models.User;
import es.jaimelozanodiegotorres.backapp.rest.user.repository.UserRepository;
import es.jaimelozanodiegotorres.backapp.rest.user.service.UserServicePgSql;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

class UserServicePgSqlTest {

    private UUID userId = UUID.randomUUID();
    private UserDto userDto = UserDto.builder()
            .name("John")
            .surname("Doe")
            .username("test")
            .email("test@example.com")
            .password("password")
            .build();
    private Addresses addresses = Addresses.builder().userId(userId).build();
    private User user = User.builder()
            .name("John")
            .surname("Doe")
            .username("test")
            .email("test@example.com")
            .password("password")
            .id(userId)
            .roles(new HashSet<>(Collections.singleton(Role.USER)))
            .build();
    private Order order = Order.builder().userId(userId).build();

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressesRepository addressesRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServicePgSql userService;


    @BeforeEach
    void setUp() {
        // Crea un mock de SecurityContext y Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        // Configura el SecurityContext para devolver el Authentication mock
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        // Configura el Authentication para devolver el usuario logueado mock
        lenient().when(authentication.getPrincipal()).thenReturn(user);
        // Establece el SecurityContextHolder con el SecurityContext mock
        SecurityContextHolder.setContext(securityContext);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUser_Success() {
        when(userRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCaseAndDeletedAtIsNull(any(), any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);

        User savedUser = userService.save(userDto);

        assertAll("savedUser",
                () -> assertNotNull(savedUser),
                () -> assertEquals(userDto.getName(), savedUser.getName()),
                () -> assertEquals(userDto.getUsername(), savedUser.getUsername())
        );
    }

    @Test
    void updateUser_Success() {
        when(userRepository.findByIdAndDeletedAtIsNull(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCaseAndDeletedAtIsNull(any(), any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);

        User updatedUser = userService.update(userId, userDto);

        assertAll("updatedUser",
                () -> assertNotNull(updatedUser),
                () -> assertEquals(userId, updatedUser.getId()),
                () -> assertEquals(userDto.getUsername(), updatedUser.getUsername())
        );
    }

    @Test
    void deleteUser_Success() {
        User existingUser = new User();
        existingUser.setId(userId);
        when(userRepository.findByIdAndDeletedAtIsNull(userId)).thenReturn(Optional.of(existingUser));
        when(addressesRepository.findByUserIdAndDeletedAtIsNull(userId)).thenReturn(List.of(addresses));
        doNothing().when(addressesRepository).deleteById(any());

        assertTrue(userService.deleteById(userId));
    }

    @Test
    void details_Success() {
        when(userRepository.findByIdAndDeletedAtIsNull(userId)).thenReturn(Optional.of(user));
        when(addressesRepository.findByUserIdAndDeletedAtIsNull(userId)).thenReturn(List.of(addresses));
        when(orderRepository.findByUserId(any())).thenReturn(List.of(order));
        UserResponseDto userDetails = userService.details(user);

        assertAll("userDetails",
                () -> assertNotNull(userDetails),
                () -> assertEquals(user.getUsername(), userDetails.getUsername()),
                () -> assertTrue(userDetails.getRoles().contains(Role.USER))
        );
    }
}
