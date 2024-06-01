package es.jaimelozanodiegotorres.backapp.rest.user.service;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.addresses.models.Addresses;
import es.jaimelozanodiegotorres.backapp.rest.addresses.repository.AddressesRepository;
import es.jaimelozanodiegotorres.backapp.rest.addresses.services.AddressesServicePgSqlImpl;
import es.jaimelozanodiegotorres.backapp.rest.commons.services.CommonServicePgSql;
import es.jaimelozanodiegotorres.backapp.rest.orders.repository.OrderRepository;
import es.jaimelozanodiegotorres.backapp.rest.user.dto.UserDto;
import es.jaimelozanodiegotorres.backapp.rest.user.dto.UserResponseDto;
import es.jaimelozanodiegotorres.backapp.rest.user.filters.UserFilters;
import es.jaimelozanodiegotorres.backapp.rest.user.mapper.UserResponseMapper;
import es.jaimelozanodiegotorres.backapp.rest.user.mapper.UserSaveMapper;
import es.jaimelozanodiegotorres.backapp.rest.user.models.User;
import es.jaimelozanodiegotorres.backapp.rest.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@CacheConfig(cacheNames = {"usuarios"})
@Slf4j
public class UserServicePgSql extends CommonServicePgSql<User, UUID> {
    private final PasswordEncoder passwordEncoder;
    private final UserSaveMapper saveMapper;
    private final UserResponseMapper responseMapper;

    private final AddressesRepository addressesRepository;
    private final OrderRepository orderRepository;
    private final AddressesServicePgSqlImpl addressesService;


    protected UserServicePgSql(UserRepository repository, PasswordEncoder passwordEncoder, AddressesRepository addressesRepository, OrderRepository orderRepository, AddressesServicePgSqlImpl addressesService) {
        super(repository);
        this.orderRepository = orderRepository;
        this.addressesService = addressesService;
        this.saveMapper = UserSaveMapper.INSTANCE;
        this.responseMapper = UserResponseMapper.INSTANCE;
        this.passwordEncoder = passwordEncoder;
        this.addressesRepository = addressesRepository;
    }

    public User save(UserDto dto){
        log.info("Guardando usuario");
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        ((UserRepository)repository).findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCaseAndDeletedAtIsNull(dto.getUsername(),dto.getEmail())
                .ifPresent(u -> {
                    throw exceptionService.badRequestException("Ya existe un usuario con ese username o email");
                });
        return save(saveMapper.dtoToModel(dto));
    }

    public User update(UUID id, UserDto dto){
        log.info("Actualizando usuario");
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        User original = findById(id);
        ((UserRepository)repository).findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCaseAndDeletedAtIsNull(dto.getUsername(),dto.getEmail())
                .ifPresent(u -> {
                    if (!u.getId().equals(id)) {
                        System.out.println("usuario encontrado: " + u.getId() + " Mi id: " + id);
                        throw exceptionService.badRequestException("Ya existe un usuario con ese username o email");
                    }
                });
        return update(saveMapper.updateModel(original, dto));
    }

    public PageResponse<User> pageAll(UserFilters filters){
        log.info("Paginando todos los usuarios");
        Page<User> page = repository.findAll(filters.getSpecifications(), filters.getPageable());
        return PageResponse.of(page, filters.getSortBy(), filters.getDirection());
    }

    /**
     * Borra un usuario por su ID (borrado lógico)
     * @param id ID del usuario a borrar
     */
    @Transactional
    @CacheEvict(key = "#id")
    @Override
    public boolean deleteById(UUID id) {
        User user = findById(id);
        deleteUserAddresesByUser(user);

        repository.deleteById(id);

        return true;
    }

    @Transactional
    public void deleteUserAddresesByUser(User user) {
        log.info("Borrado logico de las direcciones asociadas al usuario con id: {}", user.getId());

        List<Addresses> addresses = addressesRepository.findByUserIdAndDeletedAtIsNull(user.getId());
        if( addresses != null )
            addresses.forEach(address -> addressesRepository.deleteById(address.getId()));
    }

    public UserResponseDto details(User user) {
        log.info("Devolviendo los Datos del usuario con id: {}", user.getId());
        UserResponseDto res = responseMapper.modelToDto(user);

        verifyLogguedSameUser(user);

        if (user.isWorker()){
            return res;
        }

        if(user.isClient()){
            res.setOrders(orderRepository.findByUserId(user.getId()));
            res.setAddresses(addressesService.findByUserId(user.getId()));
            return res;
        }

        //tal vez se implemente logica más adelante
        throw exceptionService.badRequestException();
    }
}
