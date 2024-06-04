package es.jaimelozanodiegotorres.backapp.rest.orders.websocket;

public record Notificacion<T>(
        String entity,
        Tipo type,
        T data,
        String createdAt
) {

    public enum Tipo {CREATE, UPDATE, DELETE}

}
