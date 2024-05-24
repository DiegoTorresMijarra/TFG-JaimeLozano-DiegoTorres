package es.jaimelozanodiegotorres.backapp.rest.orders.repository;

import es.jaimelozanodiegotorres.backapp.rest.commons.repository.CommonRepositoryMongo;
import es.jaimelozanodiegotorres.backapp.rest.orders.models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CommonRepositoryMongo<Order, ObjectId> {
    @Override
    default String getTableName(){
        return "order";
    }

    Page<Order> findByRestaurantId (Long restaurantId, Pageable pageable);
    Boolean existsByRestaurantId (Long restaurantId);
}
