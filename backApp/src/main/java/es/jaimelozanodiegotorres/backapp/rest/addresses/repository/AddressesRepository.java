package es.jaimelozanodiegotorres.backapp.rest.addresses.repository;

import es.jaimelozanodiegotorres.backapp.rest.addresses.models.Addresses;
import es.jaimelozanodiegotorres.backapp.rest.commons.repository.CommonRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AddressesRepository extends CommonRepository<Addresses, UUID> {
    @Override
    default String getTableName(){
        return "Addresses";
    }

    @Query(value = "SELECT a FROM ADDRESSES a where a.userId = :userId AND a.deletedAt IS NULL")
    List<Addresses> findByUserIdAndDeletedAtIsNull(UUID userId);
}
