package es.jaimelozanodiegotorres.backapp.rest.category.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)

public class Category {
    public static final Category SIN_CATEGORIA = Category.builder() //no es igual a la de la base
            .id(-1L)
            .name("NOT_ASSIGNED")
            .build();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador de la categoria", example = "1")
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false,columnDefinition = "VARCHAR (13) CONSTRAINT CHECK_NAME CHECK name IN ('MANAGER','COOKER','CLEANER','WAITER','NOT_ASSIGNED')") //podria ser unique
    @Schema(description = "Nombre de la categoria", example = "MANAGER")
    private String name;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    @Column ( name="created_at")
    @Schema(description = "Fecha de creación de la categoria", example = "2022-01-01 00:00:00")
    private LocalDateTime createdAt = LocalDateTime.now();
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default()
    @Column (name = "updated_at")
    @Schema(description = "Fecha de actualización de la categoria", example = "2022-01-01 00:00:00")
    private LocalDateTime updatedAt = LocalDateTime.now();
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    @Schema(description = "The date when the entity is deleted", example = "2022-01-01 00:00:00")
    private LocalDate deletedAt;

    @OneToMany(mappedBy = "productos")
    @JsonBackReference
    @Schema(description = "Lista de productos")
    private List<Product> products;
}
