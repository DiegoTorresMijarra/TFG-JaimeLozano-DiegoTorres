package es.jaimelozanodiegotorres.backapp.rest.orders.dto;

import es.jaimelozanodiegotorres.backapp.rest.addresses.models.Addresses;
import es.jaimelozanodiegotorres.backapp.rest.orders.models.OrderedProduct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    // private UUID workerUUID;
    private UUID userId;

    @NotNull(message = "restaurantId no puede estar vacio")
    private Long restaurantId;

    @NotNull(message = "addressesId no puede estar vacio")
    private UUID addressesId;

    private List<@Valid OrderedProduct> orderedProducts;

}
