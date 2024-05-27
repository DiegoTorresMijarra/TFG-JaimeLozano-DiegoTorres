package es.jaimelozanodiegotorres.backapp.rest.restaurants.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos para la creación del restaurante
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDto {
    private static final String REGEXP_SANITIZER = "^[\\d\\p{L}\\s,.;:]*$";

    @Schema(description = "Nombre del restaurante", example = "McDonalds")
    @NotBlank(message = "El nombre no puede estar en blanco")
    @Pattern(regexp = REGEXP_SANITIZER, message = "El campo solo puede contener letras y números")
    private String name;

    @Schema(description = "Dirección del restaurante",example = "Calle 123")
    @NotBlank(message = "La dirección no puede estar en blanco")
    @Size(max = 255,message = "La dirección del restaurante no puede tener más de 255 caracteres")
    @Pattern(regexp = REGEXP_SANITIZER, message = "El campo solo puede contener letras y números")
    private String address;

    @Schema(description = "Telefono del restaurante", example = "123456789")
    @NotNull(message = "El número no puede estar en blanco")
    @Pattern(regexp="\\d{9}", message = "Debe tener 9 dígitos")
    private String phone;
}
