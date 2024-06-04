package es.jaimelozanodiegotorres.backapp.rest.orders.websocket;

import es.jaimelozanodiegotorres.backapp.rest.orders.models.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapperNotificacion {
    public static OrderResponseNotificacion toResponse(Order order){
        return new OrderResponseNotificacion(
                order.getId(),
                order.getRestaurantId().toString(),
                order.getUserId().toString(),
                order.getCreatedAt().toString(),
                order.getUpdatedAt().toString());
    }
}
