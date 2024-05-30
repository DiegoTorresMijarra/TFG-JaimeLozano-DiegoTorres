package es.jaimelozanodiegotorres.backapp.rest.user.mapper;

import es.jaimelozanodiegotorres.backapp.rest.commons.mapper.CommonMapper;
import es.jaimelozanodiegotorres.backapp.rest.user.dto.UserDto;
import es.jaimelozanodiegotorres.backapp.rest.user.dto.UserResponseDto;
import es.jaimelozanodiegotorres.backapp.rest.user.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserResponseMapper extends CommonMapper<User, UserResponseDto> {
    UserResponseMapper INSTANCE = Mappers.getMapper( UserResponseMapper.class );
}
