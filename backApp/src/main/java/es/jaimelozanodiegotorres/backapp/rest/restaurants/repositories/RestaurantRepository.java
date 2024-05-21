package es.jaimelozanodiegotorres.backapp.rest.restaurants.repositories;

import es.jaimelozanodiegotorres.backapp.rest.commons.repository.CommonRepository;
import es.jaimelozanodiegotorres.backapp.rest.restaurants.modelos.Restaurant;
import org.springframework.stereotype.Repository;

/**
 * Interfaz de repositorio de la entidad Restaurant
 * Extiende de JpaRepository para obtener los métodos básicos de un CRUD
 * y JpaSpecificationExecutor para obtener los métodos de especificación de JPA
 */
@Repository
public interface RestaurantRepository extends CommonRepository<Restaurant, Long> {
    @Override
    default String getTableName(){
        return "RESTAURANTS";
    }
}
