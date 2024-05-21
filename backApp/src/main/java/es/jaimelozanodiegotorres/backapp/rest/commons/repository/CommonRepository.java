package es.jaimelozanodiegotorres.backapp.rest.commons.repository;

import org.hibernate.annotations.processing.SQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface CommonRepository <T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    List<T> findByDeletedAtIsNotNull();
    List<T> findByDeletedAtIsNull();
    Optional<T> findByIdAndDeletedAtIsNull(ID id);
    String getTableName();

//    @Modifying
//    @Query("UPDATE #{#entityName} e SET e.deleted_at = (CURRENT_TIMESTAMP) WHERE e.id =: id")
//    boolean softDeleted(@Param("id") ID id);
}
