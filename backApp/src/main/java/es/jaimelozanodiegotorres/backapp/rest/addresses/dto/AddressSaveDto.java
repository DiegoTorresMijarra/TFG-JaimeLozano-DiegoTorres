package es.jaimelozanodiegotorres.backapp.rest.addresses.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Objeto de transferencia de datos para la creación de direcciones
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressSaveDto {
    private static final String REGEXP_SANITIZER = "^[\\d\\p{L}\\s,.;:]*$";

    @Schema(description = "Pais de la direccion", example = "España")
    @NotBlank(message = "El país no puede estar vacío")
    @Size(max = 50, message = "El país no puede tener más de 50 caracteres")
    @Pattern(regexp = REGEXP_SANITIZER, message = "El campo solo puede contener letras y números")
    @Builder.Default
    private String country = "España";

    @Schema(description = "Provincia de la direccion", example = "Madrid")
    @NotBlank(message = "La provincia no puede estar vacía")
    @Size(max = 50, message = "La provincia no puede tener más de 50 caracteres")
    @Pattern(regexp = REGEXP_SANITIZER, message = "El campo solo puede contener letras y números")
    @Builder.Default
    private String province = "Madrid";

    @Schema(description = "Ciudad de la direccion", example = "Leganés")
    @NotBlank(message = "La ciudad no puede estar vacía")
    @Size(max = 50, message = "La ciudad no puede tener más de 50 caracteres")
    @Pattern(regexp = REGEXP_SANITIZER, message = "El campo solo puede contener letras y números")
    private String city;

    @Schema(description = "Calle de la direccion", example = "Rioja")
    @NotBlank(message = "La calle no puede estar vacía")
    @Size(max = 100, message = "La calle no puede tener más de 100 caracteres")
    @Pattern(regexp = REGEXP_SANITIZER, message = "El campo solo puede contener letras y números")
    private String street;

    @Schema(description = "Numero del portal de la direccion", example = "101")
    @NotBlank(message = "El número no puede estar vacío")
    @Size(max = 10, message = "El número no puede tener más de 10 caracteres")
    @Pattern(regexp = REGEXP_SANITIZER, message = "El campo solo puede contener letras y números")
    private String number;

    @Schema(description = "Apartamento del edificio de la direccion", example = "Bajo C")
    @Size(max = 50, message = "El apartamento no puede tener más de 50 caracteres")
    @Pattern(regexp = REGEXP_SANITIZER, message = "El campo solo puede contener letras y números")
    private String apartment;

    @Schema(description = "Codigo Postal de la direccion", example = "28915")
    @NotBlank(message = "El código postal no puede estar vacío")
    @Pattern(regexp = "\\d{5}", message = "El código postal debe tener 5 dígitos")
    private String postalCode;

    @Schema(description = "Info Adicional de la direccion", example = "Cuidado con el Perro")
    @Size(max = 255, message = "La información adicional no puede tener más de 255 caracteres")
    @Pattern(regexp = REGEXP_SANITIZER, message = "El campo solo puede contener letras y números")
    private String extraInfo;

    @Schema(description = "Nombre de la direccion", example = "Casa de la familia torres")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    @Pattern(regexp = REGEXP_SANITIZER, message = "El campo solo puede contener letras y números")
    private String name;

    @Schema(description = "Identificador del usuario al que pertenece la direccion", example = "d7e3d74c-2a93-4c87-b66d-51e2b02a9745")
    @NotNull(message = "Identificador del usuario al que pertenece no puede ser null y tiene que ser valido")
    private UUID userId;
}
