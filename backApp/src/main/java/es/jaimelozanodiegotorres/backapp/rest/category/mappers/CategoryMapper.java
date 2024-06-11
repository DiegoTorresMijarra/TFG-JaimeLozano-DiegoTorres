package es.jaimelozanodiegotorres.backapp.rest.category.mappers;

import es.jaimelozanodiegotorres.backapp.rest.category.dto.CategoryDto;
import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;
import es.jaimelozanodiegotorres.backapp.rest.commons.mapper.CommonMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper extends CommonMapper<Category, CategoryDto> {
    CategoryMapper INSTANCE = Mappers.getMapper( CategoryMapper.class );
}
