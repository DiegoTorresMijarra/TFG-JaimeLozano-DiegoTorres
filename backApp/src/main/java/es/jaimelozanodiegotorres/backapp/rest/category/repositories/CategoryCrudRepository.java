package es.jaimelozanodiegotorres.backapp.rest.category.repositories;

import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryCrudRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
}
