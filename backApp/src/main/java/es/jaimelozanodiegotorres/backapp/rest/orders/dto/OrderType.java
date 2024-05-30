package es.jaimelozanodiegotorres.backapp.rest.orders.dto;

import es.jaimelozanodiegotorres.backapp.rest.orders.models.OrderedProduct;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface OrderType {
    //UUID getWorkerUUID();
    UUID getUserId();
    Long getRestaurantId();
    UUID getAddressesId();
    List<OrderedProduct> getOrderedProducts();
}
