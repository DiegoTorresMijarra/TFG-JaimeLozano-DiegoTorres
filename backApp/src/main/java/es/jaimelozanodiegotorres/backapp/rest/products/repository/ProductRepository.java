package es.jaimelozanodiegotorres.backapp.rest.products.repository;


import es.jaimelozanodiegotorres.backapp.rest.commons.repository.CommonRepository;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de productos que extiende de JpaRepository y JpaSpecificationExecutor
 * para poder realizar operaciones de persistencia sobre la base de datos.
 * También se utiliza la anotación @Repository para indicar que es un repositorio de Spring.
 */
@Repository
public interface ProductRepository extends CommonRepository<Product,Long> {
    @Override
    default String getTableName(){
        return "Productos";
    }
}
