package es.jaimelozanodiegotorres.backapp.rest.orders.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.jaimelozanodiegotorres.backapp.rest.orders.dto.OrderType;
import jakarta.persistence.EntityListeners;
import org.springframework.data.annotation.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Modelo de datos para los pedidos
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("order")
@TypeAlias("Order")
@EntityListeners(AuditingEntityListener.class)
public class Order implements OrderType {

    @Id
    @Builder.Default
    private ObjectId id = new ObjectId();

    //@NotNull(message = "El UUID del cliente no puede estar vacio")
    //private UUID workerUUID;

    @NotNull(message = "El UUID del cliente no puede estar vacio")
    private UUID userId;

    @NotNull(message = "El id del restaurante no puede ser nulo")
    private Long restaurantId;

    @NotNull(message = "El addressesId no puede ser nulo")
    private UUID addressesId;

    @NotNull(message = "El pedido debe tener al menos una línea de pedido")
    private List<@Valid OrderedProduct> orderedProducts;

    @Builder.Default
    private Double totalPrice = 0.0;

    @Builder.Default
    private Integer totalQuantityProducts = 0;

    @Builder.Default
    private Boolean isPaid = false;

    @Builder.Default
    private OrderState state = OrderState.PENDING;

    @CreationTimestamp
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime deletedAt = null;

    @JsonProperty("id")
    public String getId() {
        return id.toHexString();
    }

    /**
     *  Establece la lista de productos pedidos
     * @param orderedProducts lista de productos pedidos
     */
    public void setOrderedProducts(List<@Valid OrderedProduct> orderedProducts){
        this.orderedProducts= orderedProducts;
        this.totalPrice = orderedProducts.stream().mapToDouble(OrderedProduct::getTotalPrice).sum();
        this.totalQuantityProducts = orderedProducts.stream().mapToInt(OrderedProduct::getQuantity).sum();
    }

    @JsonIgnore
    public boolean isDeleteable(){
        return OrderState.isDeleteable(this.getState());
    }

    @JsonIgnore
    public boolean isUpdatable(){
        return OrderState.isUpdatable(this.getState());
    }
}
