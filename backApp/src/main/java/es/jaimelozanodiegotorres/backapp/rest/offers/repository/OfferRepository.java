package es.jaimelozanodiegotorres.backapp.rest.offers.repository;

import es.jaimelozanodiegotorres.backapp.rest.commons.repository.CommonRepository;
import es.jaimelozanodiegotorres.backapp.rest.offers.models.Offer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends CommonRepository<Offer,Long>{
    @Override
    default String getTableName(){
        return "offers";
    }

    @Query("SELECT w FROM OFFERS w WHERE w.product.id = :productId AND w.fechaDesde <= CURRENT_TIMESTAMP AND w.fechaHasta >= CURRENT_TIMESTAMP AND w.deletedAt IS NULL")
    public Offer findActivasByProduct(Long productId);
}
