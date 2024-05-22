package es.jaimelozanodiegotorres.backapp.rest.products.mapper;


import es.jaimelozanodiegotorres.backapp.rest.commons.mapper.CommonMapper;
import es.jaimelozanodiegotorres.backapp.rest.products.dto.ProductSaveDto;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapeo de un producto
 */
@Mapper
public interface ProductMapper extends CommonMapper<Product, ProductSaveDto> {
    ProductMapper INSTANCE = Mappers.getMapper( ProductMapper.class );
}
