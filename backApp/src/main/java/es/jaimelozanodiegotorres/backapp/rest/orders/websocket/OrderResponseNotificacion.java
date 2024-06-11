package es.jaimelozanodiegotorres.backapp.rest.orders.websocket;

public record OrderResponseNotificacion(
        String orderId, String restaurantId, String clientId, String addressId,
        String createdAt, String updatedAt) {
}
