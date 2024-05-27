package es.jaimelozanodiegotorres.backapp.rest.orders.models;

public enum OrderState {
    PENDING,
    ACCEPTED,
    REJECTED,
    DELIVERED,
    CANCELED,
    DELETED;

    public static boolean isDeleteable(OrderState state) { // Prefiero que se vea para verlo más claro
        return switch (state) {
            case PENDING, ACCEPTED, REJECTED, CANCELED, DELETED -> true;
            case DELIVERED -> false;
        };
    }

    public static boolean isUpdatable(OrderState state) {
        return switch (state) {
            case PENDING, ACCEPTED, REJECTED -> true;
            case DELETED, CANCELED, DELIVERED -> false;
        };
    }
}
