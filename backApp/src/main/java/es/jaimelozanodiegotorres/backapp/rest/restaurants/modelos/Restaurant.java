package es.jaimelozanodiegotorres.backapp.rest.restaurants.modelos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeletes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Clase que representa el modelo de datos de un restaurante
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity(name="RESTAURANTS")
@Table(name="RESTAURANTS")
@SQLDelete(sql = "UPDATE RESTAURANTS SET deleted_at = CURRENT_TIMESTAMP WHERE id=?")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del restaurante",example = "1")
    private Long id;

    @Schema(description = "Nombre del restaurante",example = "Mcdonalds")
    @Column
    @NotBlank(message = "El nombre no puede estar en blanco")
    private String name;

    @Schema(description = "Telefono del restaurante",example = "123456789")
    @Column
    @NotNull(message = "El numero no puede estar en blanco")
    @Pattern(regexp="\\d{9}", message = "Debe tener 9 dígitos")
    private String phone;

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
}
