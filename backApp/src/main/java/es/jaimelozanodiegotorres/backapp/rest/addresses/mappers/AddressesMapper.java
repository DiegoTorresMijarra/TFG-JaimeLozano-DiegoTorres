package es.jaimelozanodiegotorres.backapp.rest.addresses.mappers;

import es.jaimelozanodiegotorres.backapp.rest.addresses.dto.AddressSaveDto;
import es.jaimelozanodiegotorres.backapp.rest.addresses.models.Addresses;
import es.jaimelozanodiegotorres.backapp.rest.commons.mapper.CommonMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressesMapper extends CommonMapper<Addresses, AddressSaveDto> {
    AddressesMapper INSTANCE = Mappers.getMapper( AddressesMapper.class );
}
