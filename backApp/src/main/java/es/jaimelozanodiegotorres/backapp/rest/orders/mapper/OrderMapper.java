package es.jaimelozanodiegotorres.backapp.rest.orders.mapper;

import es.jaimelozanodiegotorres.backapp.rest.commons.mapper.CommonMapper;
import es.jaimelozanodiegotorres.backapp.rest.orders.dto.OrderDto;
import es.jaimelozanodiegotorres.backapp.rest.orders.models.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper extends CommonMapper<Order, OrderDto> {
    OrderMapper INSTANCE = Mappers.getMapper( OrderMapper.class );
}
