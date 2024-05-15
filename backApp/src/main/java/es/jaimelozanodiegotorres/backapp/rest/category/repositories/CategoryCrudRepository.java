package es.jaimelozanodiegotorres.backapp.rest.category.repositories;

import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryCrudRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    /**
     * Busca una categoria por su nombre
     * @param name  nombre de la categoria a buscar
     * @return categoria encontrado
     */
    @Query("SELECT w FROM Category w WHERE w.name LIKE :name")
    Optional<Category> findByName(String name);
}
