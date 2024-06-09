package es.jaimelozanodiegotorres.backapp.rest.addresses.services;

import es.jaimelozanodiegotorres.backapp.rest.addresses.dto.AddressSaveDto;
import es.jaimelozanodiegotorres.backapp.rest.addresses.mappers.AddressesMapper;
import es.jaimelozanodiegotorres.backapp.rest.addresses.models.Addresses;
import es.jaimelozanodiegotorres.backapp.rest.addresses.repository.AddressesRepository;
import es.jaimelozanodiegotorres.backapp.rest.commons.repository.CommonRepository;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class AddressesServicePgSqlImplTest {

    @Mock
    private AddressesRepository addressesRepository;

    @Mock
    private CommonRepository<Addresses, UUID> commonRepository;

    @InjectMocks
    private AddressesServicePgSqlImpl addressesService;

    private final AddressesMapper addressesMapper = AddressesMapper.INSTANCE;

    private final UUID addressId = UUID.fromString("37bf14f0-db5d-4f12-bf4a-7b780cfac071");
    private final UUID userId = UUID.fromString("24bee18d-920c-4f25-971f-99e91d0aa331");

    private final Addresses address1 = Addresses.builder()
            .id(addressId)
            .country("España")
            .province("Madrid")
            .city("Leganés")
            .street("Rioja")
            .number("101")
            .apartment("Bajo C")
            .postalCode("28915")
            .extraInfo("Cuidado con el Perro")
            .name("Casa de la familia torres")
            .userId(userId)
            .build();

    private final AddressSaveDto newAddressDto = AddressSaveDto.builder()
            .country("España")
            .province("Madrid")
            .city("Leganés")
            .street("Rioja")
            .number("102")
            .apartment("1ºA")
            .postalCode("28915")
            .extraInfo("Nada relevante")
            .name("Oficina")
            .build();

    @BeforeEach
    void setUp() {
        // Crea un mock de SecurityContext y Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = User.builder()
                .id(userId)
                .roles(Set.of(Role.USER))
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
        Addresses newAddress = addressesMapper.dtoToModel(newAddressDto);
        newAddress.setUserId(userId);
        when(addressesRepository.save(any(Addresses.class))).thenReturn(newAddress);

        Addresses result = addressesService.save(newAddressDto);

        assertAll("Save",
                () -> assertNotNull(result),
                () -> assertEquals(newAddress.getId(), result.getId()),
                () -> verify(addressesRepository, times(1)).save(any(Addresses.class))
        );
    }

    @Test
    void update() {
        when(addressesRepository.findByIdAndDeletedAtIsNull(address1.getId())).thenReturn(Optional.of(address1));
        Addresses updatedAddress = addressesMapper.updateModel(address1, newAddressDto);
        when(addressesRepository.save(updatedAddress)).thenReturn(updatedAddress);

        Addresses result = addressesService.update(address1.getId(), newAddressDto);

        assertAll("Update",
                () -> assertNotNull(result),
                () -> assertEquals(address1.getId(), result.getId()),
                () -> verify(addressesRepository, times(1)).findByIdAndDeletedAtIsNull(address1.getId()),
                () -> verify(addressesRepository, times(1)).save(updatedAddress)
        );
    }

    @Test
    void delete() {
        when(addressesRepository.findByIdAndDeletedAtIsNull(address1.getId())).thenReturn(Optional.of(address1));
        doNothing().when(addressesRepository).deleteById(address1.getId());

        boolean result = addressesService.deleteById(address1.getId());

        assertAll("Delete",
                () -> assertTrue(result),
                () -> verify(addressesRepository, times(1)).findByIdAndDeletedAtIsNull(address1.getId()),
                () -> verify(addressesRepository, times(1)).deleteById(address1.getId())
        );
    }

    @Test
    void findByUserId() {
        List<Addresses> expectedAddresses = Arrays.asList(address1);
        when(addressesRepository.findByUserIdAndDeletedAtIsNull(userId)).thenReturn(expectedAddresses);

        List<Addresses> result = addressesService.findByUserId(userId);

        assertAll("FindByUserId",
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(expectedAddresses.size(), result.size()),
                () -> verify(addressesRepository, times(1)).findByUserIdAndDeletedAtIsNull(userId)
        );
    }

    @Test
    void findById() {
        when(addressesRepository.findByIdAndDeletedAtIsNull(address1.getId())).thenReturn(Optional.of(address1));

        Addresses result = addressesService.findById(address1.getId());

        assertAll("FindById",
                () -> assertNotNull(result),
                () -> assertEquals(address1.getId(), result.getId()),
                () -> verify(addressesRepository, times(1)).findByIdAndDeletedAtIsNull(address1.getId())
        );
    }
}
