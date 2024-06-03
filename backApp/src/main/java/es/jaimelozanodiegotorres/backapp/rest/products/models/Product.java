package es.jaimelozanodiegotorres.backapp.rest.products.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.models.Evaluation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase que representa el modelo de datos de un producto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "PRODUCTS")
@Table(name = "PRODUCTS")
@SQLDelete(sql = "UPDATE PRODUCTS SET deleted_at = CURRENT_TIMESTAMP WHERE id=?")
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador del producto", example = "1")
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre del producto", example = "Cafe")
    private String name;

    @Column(nullable = false)
    @Positive(message = "El precio no puede ser negativo")
    @NotNull(message = "El precio no puede estar vacío")
    @Schema(description = "Precio del producto", example = "1.0")
    private double price;

    @Column(nullable = false)
    @PositiveOrZero(message = "El stock no puede ser negativo")
    @NotNull(message = "El stock no puede estar vacío")
    @Schema(description = "Stock del producto", example = "1")
    private Integer stock;

    @Column()
    @Builder.Default
    @Schema(description = "Es un producto de Glutens", example = "true")
    private boolean gluten = true;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    @Column (name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de creación de la categoria", example = "2022-01-01 00:00:00")
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    @Column (name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de actualización de la categoria", example = "2022-01-01 00:00:00")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    @Schema(description = "The date when the entity is deleted", example = "null")
    private LocalDateTime deletedAt;

    @Schema(description = "Categoria del producto", example = "1")
    @ManyToOne
    @JsonManagedReference
    @ToString.Exclude
    @JoinColumn(name = "category_id")
    private Category category;

//    @Schema(description = "Valoraciones del producto")
//    @OneToMany(mappedBy = "id")
//    @JsonManagedReference
//    private List<Evaluation> evaluations;

    @Formula(value = "(SELECT AVG(e.value) FROM EVALUATION e WHERE e.product_id = id)")
    @Schema(description = "Calificación promedio del producto", example = "4.5")
    private Double averageRating;
}
