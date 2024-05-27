package es.jaimelozanodiegotorres.backapp.rest.addresses.services;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.addresses.dto.AddressSaveDto;
import es.jaimelozanodiegotorres.backapp.rest.addresses.mappers.AddressesMapper;
import es.jaimelozanodiegotorres.backapp.rest.addresses.models.Addresses;
import es.jaimelozanodiegotorres.backapp.rest.addresses.repository.AddressesRepository;
import es.jaimelozanodiegotorres.backapp.rest.category.filters.CategoryFilters;
import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;
import es.jaimelozanodiegotorres.backapp.rest.commons.services.CommonService;
import es.jaimelozanodiegotorres.backapp.rest.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AddressesServiceImpl extends CommonService<Addresses, UUID> {
    private final AddressesMapper addressesMapper;
    private final UserService userService;

    @Autowired
    public AddressesServiceImpl(AddressesRepository addressesRepository, UserService userService) {
        super(addressesRepository);
        this.userService = userService;
        addressesMapper = AddressesMapper.INSTANCE;
    }

    public Addresses save(AddressSaveDto dto) {
        Addresses entity = addressesMapper.dtoToModel(dto);
        entity.setUser(userService.findById(dto.getUserId()));

        return save(entity);
    }

    public Addresses update(UUID id, AddressSaveDto dto) {
        Addresses entity = addressesMapper.updateModel(findById(id), dto);
        entity.setUser(userService.findById(dto.getUserId()));

        return update(entity);
    }

    public List<Addresses> findByUserId(UUID uuid) {
        log.info("Buscando direcciones del usuario con id: {}", uuid);
        return ((AddressesRepository)repository).findByUserIdAndDeletedAtIsNull(uuid);
    }

//    public PageResponse<Addresses> pageAll(CategoryFilters filters){ todo
//        log.info("Paginando todas las categorias");
//        Page<Category> page = repository.findAll(filters.getSpecifications(), filters.getPageable());
//        return PageResponse.of(page, filters.getSortBy(), filters.getDirection());
//    }
}
