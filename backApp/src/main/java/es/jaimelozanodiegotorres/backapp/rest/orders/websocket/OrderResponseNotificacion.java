package es.jaimelozanodiegotorres.backapp.rest.orders.websocket;

public record OrderResponseNotificacion(String orderId, String restaurantId, String clientId, String fecha_cre, String fecha_act) {
}
