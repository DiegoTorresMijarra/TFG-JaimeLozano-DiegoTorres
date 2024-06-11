package es.jaimelozanodiegotorres.backapp.rest.category.repository;

import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;
import es.jaimelozanodiegotorres.backapp.rest.commons.repository.CommonRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends CommonRepository<Category,Long> {
    @Override
    default String getTableName(){
        return "Categories";
    }
    /**
     * Busca una categoria por su nombre
     * @param name  nombre de la categoria a buscar
     * @return categoria encontrado
     */
    @Query("SELECT w FROM CATEGORIES w WHERE w.name LIKE :name")
    public Optional<Category> findByName(String name);
}
