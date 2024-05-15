package es.jaimelozanodiegotorres.backapp.rest.products.repository;

import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface CommonRepository <T, ID extends Serializable> extends JpaRepository<T, ID> {
    List<Product> findByDeletedAtIsNotNull();
    List<Product> findByDeletedAtIsNull();

//    @Modifying
//    @Query("UPDATE #{#entityName} e SET e.deleted_at = (CURRENT_TIMESTAMP) WHERE e.id =: id")
//    boolean softDeleted(@Param("id") ID id);
}
