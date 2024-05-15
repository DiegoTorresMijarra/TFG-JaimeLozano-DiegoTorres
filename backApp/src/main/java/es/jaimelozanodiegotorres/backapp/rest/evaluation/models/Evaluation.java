package es.jaimelozanodiegotorres.backapp.rest.evaluation.models;

import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador de la valoracion", example = "1")
    private Long id;

    @Column(nullable = false)
    @PositiveOrZero(message = "La valoracion no puede ser negativa")
    @NotNull(message = "La valoracion no puede estar vacía")
    @Max(value = 5)
    @Schema(description = "Valoracion del producto", example = "1")
    private Integer valoracion;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    @Column ( name="created_at")
    @Schema(description = "Fecha de creación de la valoracion", example = "2022-01-01 00:00:00")
    private LocalDateTime createdAt = LocalDateTime.now();
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default()
    @Column (name = "updated_at")
    @Schema(description = "Fecha de actualización de la valoracion", example = "2022-01-01 00:00:00")
    private LocalDateTime updatedAt = LocalDateTime.now();
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    @Schema(description = "The date when the entity is deleted", example = "Null")
    private LocalDateTime deletedAt;

    @Schema(description = "Producto al que hace refencia la valoracion", example = "1")
    @ManyToOne
    @JoinColumn(name = "product_id")
    @NotNull(message = "El producto no puede estar vacío")
    private Product product;
}
