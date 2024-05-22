package es.jaimelozanodiegotorres.backapp.rest.category.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "CATEGORIES")
@Table(name = "CATEGORIES")
@SQLDelete(sql = "UPDATE CATEGORIES SET deleted_at = CURRENT_TIMESTAMP WHERE id=?")
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
    @Column()
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
    @Schema(description = "The date when the entity is deleted", example = "Null")
    private LocalDateTime deletedAt;

    @Schema(description = "Lista de productos")
    @OneToMany(mappedBy = "id")
    @JsonBackReference
    private List<Product> products;
}
