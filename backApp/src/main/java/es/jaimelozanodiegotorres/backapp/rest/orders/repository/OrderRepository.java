package es.jaimelozanodiegotorres.backapp.rest.orders.repository;

import es.jaimelozanodiegotorres.backapp.rest.commons.repository.CommonRepositoryMongo;
import es.jaimelozanodiegotorres.backapp.rest.orders.models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends CommonRepositoryMongo<Order, ObjectId> {
    @Override
    default String getTableName(){
        return "order";
    }

    Page<Order> findByRestaurantId (Long restaurantId, Pageable pageable);
    Boolean existsByRestaurantId (Long restaurantId);

    List<Order> findByUserId(UUID id);
}
