package es.jaimelozanodiegotorres.backapp.rest.user.service;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.commons.repository.CommonRepository;
import es.jaimelozanodiegotorres.backapp.rest.commons.services.CommonService;
import es.jaimelozanodiegotorres.backapp.rest.user.dto.UserDto;
import es.jaimelozanodiegotorres.backapp.rest.user.filters.UserFilters;
import es.jaimelozanodiegotorres.backapp.rest.user.mapper.UserMapper;
import es.jaimelozanodiegotorres.backapp.rest.user.models.User;
import es.jaimelozanodiegotorres.backapp.rest.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@CacheConfig(cacheNames = {"usuarios"})
@Slf4j
public class UserService extends CommonService<User, UUID> {
    PasswordEncoder passwordEncoder;
    UserMapper mapper;


    protected UserService(CommonRepository<User, UUID> repository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.mapper = UserMapper.INSTANCE;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(UserDto dto){
        log.info("Guardando usuario");
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        ((UserRepository)repository).findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCaseAndDeletedAtIsNull(dto.getUsername(),dto.getEmail())
                .ifPresent(u -> {
                    throw exceptionService.badRequestException("Ya existe un usuario con ese username o email");
                });
        return save(mapper.dtoToModel(dto));
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
        return update(mapper.updateModel(original, dto));
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
    public Boolean deleteByIdLogico(UUID id) {
        User user = findById(id);
        //Hacemos el borrado fisico si no hay pedidos
       // if (ordersCrudRepository.existsByWorkerUUID(id)) {

            // Si no, lo marcamos como borrado lógico
            ((UserRepository)repository).deleteById(id);
        //} else {
        return true;
       // }
    }
}
