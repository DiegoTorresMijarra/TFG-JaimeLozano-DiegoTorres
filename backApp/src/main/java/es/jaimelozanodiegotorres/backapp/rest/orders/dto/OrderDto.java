package es.jaimelozanodiegotorres.backapp.rest.orders.dto;

import es.jaimelozanodiegotorres.backapp.rest.orders.models.OrderedProduct;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Modelo de datos para ACTUALIZAR los pedidos
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto implements OrderType{
    private UUID clientUUID;
    private UUID workerUUID;
    private Long restaurantId;
    private List<@Valid OrderedProduct> orderedProducts;
}
