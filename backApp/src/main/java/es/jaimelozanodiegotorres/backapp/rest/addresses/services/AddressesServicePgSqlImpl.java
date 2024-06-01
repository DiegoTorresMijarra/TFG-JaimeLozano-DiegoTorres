package es.jaimelozanodiegotorres.backapp.rest.addresses.services;

import es.jaimelozanodiegotorres.backapp.rest.addresses.dto.AddressSaveDto;
import es.jaimelozanodiegotorres.backapp.rest.addresses.mappers.AddressesMapper;
import es.jaimelozanodiegotorres.backapp.rest.addresses.models.Addresses;
import es.jaimelozanodiegotorres.backapp.rest.addresses.repository.AddressesRepository;
import es.jaimelozanodiegotorres.backapp.rest.commons.services.CommonServicePgSql;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AddressesServicePgSqlImpl extends CommonServicePgSql<Addresses, UUID> {
    private final AddressesMapper addressesMapper;

    @Autowired
    public AddressesServicePgSqlImpl(AddressesRepository addressesRepository) {
        super(addressesRepository);
        addressesMapper = AddressesMapper.INSTANCE;
    }

    public Addresses save(AddressSaveDto dto) {
        Addresses entity = addressesMapper.dtoToModel(dto);

        entity.setUserId(getLoggedUserId());

        return save(entity);
    }

    public Addresses update(UUID id, AddressSaveDto dto) {
        Addresses entity = addressesMapper.updateModel(findById(id), dto);

        verifyLogguedSameUser(entity.getUserId());

        return update(entity);
    }

    public List<Addresses> findByUserId(UUID uuid) {
        log.info("Buscando direcciones del usuario con id: {}", uuid);

//         verifyLogguedSameUser(uuid);

        return ((AddressesRepository)repository).findByUserIdAndDeletedAtIsNull(uuid);
    }

    @Override
    public boolean deleteById(UUID id) {
        log.info("Borrando {} con id: {}", entityName, id);

        Addresses entity = findById(id);
        verifyLogguedSameUser(entity.getUserId());

        repository.deleteById(id);

        return true;
    }

//    public PageResponse<Addresses> pageAll(CategoryFilters filters){ todo
//        log.info("Paginando todas las categorias");
//        Page<Category> page = repository.findAll(filters.getSpecifications(), filters.getPageable());
//        return PageResponse.of(page, filters.getSortBy(), filters.getDirection());
//    }
}
