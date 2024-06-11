package es.jaimelozanodiegotorres.backapp.rest.restaurants.mapper;

import es.jaimelozanodiegotorres.backapp.rest.commons.mapper.CommonMapper;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.dto.RestaurantDto;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.modelos.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Clase que mapea los datos de un restaurante
 */
@Mapper
public interface RestaurantMapper extends CommonMapper<Restaurant, RestaurantDto> {
    RestaurantMapper INSTANCE = Mappers.getMapper( RestaurantMapper.class );
}
