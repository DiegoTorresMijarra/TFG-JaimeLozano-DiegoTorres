package es.jaimelozanodiegotorres.backapp.rest.orders.mapper;

import es.jaimelozanodiegotorres.backapp.rest.commons.mapper.CommonMapper;
import es.jaimelozanodiegotorres.backapp.rest.orders.dto.OrderDto;
import es.jaimelozanodiegotorres.backapp.rest.orders.models.Order;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper extends CommonMapper<Order, OrderDto> {
    OrderMapper INSTANCE = Mappers.getMapper( OrderMapper.class );

    @Mapping(target = "orderedProducts", ignore = true)
    Order dtoToModel(OrderDto dto);

    @Mapping(target = "orderedProducts", ignore = true)
    Order updateModel(@MappingTarget Order original, OrderDto dto);

    @AfterMapping
    default void setOrderedProducts(@MappingTarget Order order, OrderDto dto) {
        if (dto.getOrderedProducts() != null) {
            order.setOrderedProducts(dto.getOrderedProducts());
        }
    }
}
