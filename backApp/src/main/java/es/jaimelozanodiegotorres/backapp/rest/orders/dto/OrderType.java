package es.jaimelozanodiegotorres.backapp.rest.orders.dto;

import es.jaimelozanodiegotorres.backapp.rest.orders.models.OrderedProduct;

import java.util.List;
import java.util.UUID;

public interface OrderType {
    //UUID getClientUUID();
    //UUID getWorkerUUID();
    Long getRestaurantId();
    List<OrderedProduct> getOrderedProducts();
}
