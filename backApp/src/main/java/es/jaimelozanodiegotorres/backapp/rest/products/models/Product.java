package es.jaimelozanodiegotorres.backapp.rest.products.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Clase que representa el modelo de datos de un producto
 */
@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador del producto", example = "1")
    private Long id;
    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre del producto", example = "Cafe")
    private String nombre;
    @Column(nullable = false)
    @Positive(message = "El precio no puede ser negativo")
    @NotNull(message = "El precio no puede estar vacío")
    @Schema(description = "Precio del producto", example = "1.0")
    private double precio;
    @Column(nullable = false)
    @PositiveOrZero(message = "El stock no puede ser negativo")
    @NotNull(message = "El stock no puede estar vacío")
    @Schema(description = "Stock del producto", example = "1")
    private Integer stock;
    @Column()
    @Builder.Default
    @Schema(description = "Es un producto de Glutens", example = "true")
    private boolean gluten = true;
    @Column(nullable = false, name = "created_at")
    @Schema(description = "Fecha de creación del producto", example = "2022-01-01")
    LocalDate createdAt;
    @Schema(description = "Fecha de actualización del producto", example = "2022-01-01")
    @Column(nullable = false, name = "updated_at")
    LocalDate updatedAt;
    @Column(name = "deleted_at")
    @Schema(description = "The date when the entity is deleted", example = "null")
    private LocalDate deletedAt;
}
