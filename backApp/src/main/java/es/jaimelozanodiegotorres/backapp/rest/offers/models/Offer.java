package es.jaimelozanodiegotorres.backapp.rest.offers.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity(name = "OFFERS")
@Table(name = "OFFERS")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE OFFERS SET deleted_at = CURRENT_TIMESTAMP WHERE id=?")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador de la oferta", example = "1")
    private Long id;

    @Column(nullable = false)
    @PositiveOrZero(message = "La oferta no puede ser negativa")
    @NotNull(message = "La oferta no puede estar vacía")
    @Max(value = 100)
    @Schema(description = "oferta del producto", example = "1")
    private double descuento;

    @Column(nullable = false, name="fecha_desde")
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Fecha de inicio de la oferta", example = "2022-01-01 00:00:00")
    private LocalDateTime fechaDesde;

    @Column(nullable = false, name="fecha_hasta")
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Fecha de fin de la oferta", example = "2022-01-01 00:00:00")
    private LocalDateTime fechaHasta;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    @Column ( name="created_at")
    @Schema(description = "Fecha de creación de la oferta", example = "2022-01-01 00:00:00")
    private LocalDateTime createdAt = LocalDateTime.now();
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default()
    @Column (name = "updated_at")
    @Schema(description = "Fecha de actualización de la oferta", example = "2022-01-01 00:00:00")
    private LocalDateTime updatedAt = LocalDateTime.now();

    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    @Schema(description = "The date when the entity is deleted", example = "Null")
    private LocalDateTime deletedAt;

    @Schema(description = "Producto al que hace refencia la oferta", example = "1")
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonManagedReference
    @NotNull(message = "El producto no puede estar vacío")
    private Product product;
}
