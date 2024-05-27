package es.jaimelozanodiegotorres.backapp.rest.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Petición de autenticación de usuario
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignInRequest {
    private static final String REGEXP_SANITIZER = "^[\\d\\p{L}\\s,.;:]*$";

    @Schema(description = "Username", example = "JhonDoe33")
    @NotBlank(message = "Username no puede estar vacío")
    @Pattern(regexp = REGEXP_SANITIZER, message = "El campo solo puede contener letras y números")
    private String username;

    @Schema(description = "Password", example = "Password33")
    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password debe tener al menos 5 caracteres")
    @Pattern(regexp = REGEXP_SANITIZER, message = "El campo solo puede contener letras y números") // todo, Meter un pattern más apropiado
    private String password;
}
