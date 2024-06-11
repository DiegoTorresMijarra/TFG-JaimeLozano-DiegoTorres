package es.jaimelozanodiegotorres.backapp.rest.addresses.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import es.jaimelozanodiegotorres.backapp.rest.user.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "ADDRESSES")
@Table(name = "ADDRESSES")
@SQLDelete(sql = "UPDATE ADDRESSES SET deleted_at = CURRENT_TIMESTAMP WHERE id=?")
@EntityListeners(AuditingEntityListener.class)
public class Addresses {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador de la direccion", example = "1")
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column
    @Builder.Default // de momento va a ser solo España
    @Schema(description = "Pais de la direccion", example = "España")
    private String country = "España";

    @Column
    @Builder.Default // de momento va a ser solo Madrid
    @Schema(description = "Pais de la direccion", example = "Madrid")
    private String province = "Madrid";

    @Column
    @Schema(description = "Ciudad de la direccion", example = "Leganés")
    private String city;

    @Column
    @Schema(description = "Calle de la direccion", example = "Rioja")
    private String street;

    @Column
    @Schema(description = "Numero del portal de la direccion", example = "101")
    private String number;

    @Column
    @Schema(description = "Apartamento del edificio de la direccion", example = "Bajo C")
    private String apartment;

    @Column
    @Schema(description = "Codigo Postal de la direccion", example = "28915")
    private String postalCode;

    @Column
    @Schema(description = "Info Adicional de la direccion", example = "Cuidado con el Perro")
    private String extraInfo;

    @Column
    @Schema(description = "Nombre de la direccion", example = "Casa de la familia torres")
    private String name;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    @Column ( name="created_at")
    @Schema(description = "Fecha de creación de la direccion", example = "2022-01-01 00:00:00")
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default()
    @Column (name = "updated_at")
    @Schema(description = "Fecha de actualización de la direccion", example = "2022-01-01 00:00:00")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    @Schema(description = "The date when the entity is deleted", example = "Null")
    private LocalDateTime deletedAt;

    @Column(name = "user_id")
//    @JsonIgnore
    @Schema(description = "Usuario al que hace refencia la direccion", example = "UUID")
    private UUID userId;
}
