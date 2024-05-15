package es.jaimelozanodiegotorres.backapp.rest.restaurants.mapper;

import es.jaimelozanodiegotorres.backapp.rest.commons.mapper.CommonMapper;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.dto.NewRestaurantDTO;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.dto.UpdatedRestaurantDTO;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.modelos.Restaurant;

import java.time.LocalDateTime;

/**
 * Clase que mapea los datos de un restaurante
 */
public class RestaurantMapper implements CommonMapper<Restaurant, NewRestaurantDTO , UpdatedRestaurantDTO> {

    /**
     * Mapea los datos de un restaurante nuevo a un restaurante
     * @param saveDto Datos del restaurante nuevo
     * @return Restaurante
     */
    @Override
    public Restaurant saveToModel(NewRestaurantDTO saveDto) {
        return Restaurant.builder()
                .name(saveDto.getName())
                .phone(saveDto.getNumber())
                .build();
    }

    /**
     * Mapea los datos de un restaurante actualizado a un restaurante
     * @param updateDto Datos del restaurante actualizado
     * @param original   Datos del restaurante original
     * @return Devuelve un restaurante
     */
    @Override
    public Restaurant updateToModel(Restaurant original, UpdatedRestaurantDTO updateDto) {
        return Restaurant.builder()
                .id(original.getId())
                .name(updateDto.getName())
                .phone(updateDto.getNumber())
                .createdAt(original.getCreatedAt())
                .deletedAt(original.getDeletedAt())
                .build();
    }
}
