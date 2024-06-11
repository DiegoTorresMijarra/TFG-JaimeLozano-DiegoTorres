package es.jaimelozanodiegotorres.backapp.rest.commons.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface CommonRepositoryMongo <T, ID extends Serializable> extends MongoRepository<T, ID> {
    List<T> findByDeletedAtIsNotNull();
    List<T> findByDeletedAtIsNull();
    Optional<T> findByIdAndDeletedAtIsNull(ID id);
    String getTableName();

//    @Modifying
//    @Query("UPDATE #{#entityName} e SET e.deleted_at = (CURRENT_TIMESTAMP) WHERE e.id =: id")
//    boolean softDeleted(@Param("id") ID id);
}
