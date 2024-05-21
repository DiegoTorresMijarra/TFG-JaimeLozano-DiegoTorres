package es.jaimelozanodiegotorres.backapp.rest.restaurants.servicios;

import es.jaimelozanodiegotorres.backapp.rest.commons.services.CommonService;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.dto.RestaurantDto;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.mapper.RestaurantMapper;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.modelos.Restaurant;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.repositories.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementación de la interfaz RestaurantService
 * Proporciona los métodos para gestionar los restaurantes
 * Anotamos la clase con @Service para indicar que es un servicio
 * Anotamos la clase con @CacheConfig para indicar que el nombre de la cache es "restaurants"
 * @Parameter repository Repositorio de restaurantes
 * @Parameter map Mapeador de restaurantes
 */
@CacheConfig(cacheNames = {"restaurants"})
@Service
@Slf4j
public class RestaurantServiceImpl extends CommonService<Restaurant, Long> {
    RestaurantMapper mapper;

    /**
     * Constructor de la clase
     * @param repository Repositorio de restaurantes
     */
    @Autowired
    public RestaurantServiceImpl(RestaurantRepository repository){
        super(repository);
        mapper = RestaurantMapper.INSTANCE;
    }

    /**
     * Método que obtiene todos los restaurantes que cumplan con los parámetros de búsqueda
     * @param name Opcional: nombre del restaurante
     * @param number Opcional: número de teléfono del restaurante
     * @param isDeleted Opcional: indica si el restaurante está eliminado
     * @param page informacion de la paginación
     * @return Pagina de restaurantes que cumplan con los parámetros de búsqueda
     */
    public Page<Restaurant> pageAll(Optional<String> name, Optional<String> number, Optional<Boolean> isDeleted, Pageable page) {
        log.info("Devolviendo todos los restaurantes paginados");
        //Criterio busqueda nombre
        Specification<Restaurant> speNameRes=(root, query, criteriaBuilder)->
                name.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + m.toLowerCase() + "%")) // Buscamos por nombre
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))); // Si no hay nombre, no filtramos
        //Criterio busqueda numero
        Specification<Restaurant> speNumRes = (root, query, criteriaBuilder) ->
                number.map(m -> criteriaBuilder.equal(root.get("number"), m)) // Buscamos por numero
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))); // Si no hay numero, no filtramos

        //Criterio busqueda isDeleted
        Specification<Restaurant>criterio =Specification.where(speNameRes)
                .and(speNumRes);

        return super.repository.findAll(criterio,page);
    }

    /**
     * Método que guarda un restaurante con la información de un RestauranteDTO
     * @param dto RestauranteDTO con la informacion del restaurante a guardar
     * @return Restaurante guardado
     */
    @CachePut
    public Restaurant save(RestaurantDto dto) {
        return save(mapper.dtoToModel(dto));
    }

    /**
     * Método que actualiza un restaurante con la información de un RestauranteDTO
     * @param id ID del restaurante a actualizar
     * @param restaurantDTO RestauranteDTO con la información a actualizar
     * @return Restaurante actualizado
     */
    @CachePut
    @Transactional
    public Restaurant update(Long id, RestaurantDto restaurantDTO) {
        Restaurant original = findById(id);
        return update(mapper.updateModel(original, restaurantDTO));
    }
}
