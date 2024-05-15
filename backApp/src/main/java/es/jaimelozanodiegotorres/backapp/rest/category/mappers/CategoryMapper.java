package es.jaimelozanodiegotorres.backapp.rest.category.mappers;


import es.jaimelozanodiegotorres.backapp.rest.category.dto.CategoryResponseDto;
import es.jaimelozanodiegotorres.backapp.rest.category.dto.CategorySaveDto;
import es.jaimelozanodiegotorres.backapp.rest.category.dto.CategoryUpdateDto;
import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;

/**
 * Mapeador de la entidad Position
 */
public class CategoryMapper {
    /**
     * Convierte un objeto PositionSaveDto a un objeto Position
     * @param positionSaveDto  objeto CategorySaveDto
     * @return objeto Category
     */
    public static Category toModel(CategorySaveDto positionSaveDto) {
        return Category.builder()
                .name(positionSaveDto.getName())
                .build();
    }

    /**
     * Actualiza una categoria con los datos de CategoryUpdateDto
     * insertando en el nuevo objeto los datos del original si no se han modificado en el CategoryUpdateDto
     * @param original objeto Category
     * @param categoryUpdateDto objeto CategoryUpdateDto
     * @return objeto Category actualizado
     */
    public static Category toModel(Category original, CategoryUpdateDto categoryUpdateDto) {
        return Category.builder()
                .id(original.getId())
                .name(categoryUpdateDto.getName())
                .createdAt(original.getCreatedAt())
                .build();
    }

    /**
     * Convierte un objeto Position a un objeto PositionResponseDto
     * @param category   objeto Category
     * @return  objeto CategoryResponseDto
     */
    public static CategoryResponseDto toPositionResponseDto(Category category){
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .deletedAt(category.getDeletedAt())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
