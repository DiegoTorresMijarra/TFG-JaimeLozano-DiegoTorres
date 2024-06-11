package es.jaimelozanodiegotorres.backapp.rest.user.dto;

import es.jaimelozanodiegotorres.backapp.rest.user.models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private static final String REGEXP_SANITIZER = "^[\\d\\p{L}\\s,.;:]*$";

    @Schema(description = "Nombre del usuario", example = "John")
    @NotBlank(message = "Nombre no puede estar vacío")
    @Pattern(regexp = REGEXP_SANITIZER, message = "El campo solo puede contener letras y números")
    private String name;

    @Schema(description = "Apellidos del usuario", example = "Doe")
    @NotBlank(message = "Apellidos no puede estar vacío")
    @Pattern(regexp = REGEXP_SANITIZER, message = "El campo solo puede contener letras y números")
    private String surname;

    @Schema(description = "Username del usuario", example = "johndoe")
    @NotBlank(message = "Username no puede estar vacío")
    @Pattern(regexp = REGEXP_SANITIZER, message = "El campo solo puede contener letras y números")
    private String username;

    @Schema(description = "Email del usuario", example = "johndoe@localhost")
    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    @NotBlank(message = "Email no puede estar vacío")
    private String email;

    @Schema(description = "Password del usuario", example = "password")
    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password debe tener al menos 5 caracteres")
    @Size(min = 5, message = "Password debe tener al menos 5 caracteres")
    @Pattern(regexp = REGEXP_SANITIZER, message = "El campo solo puede contener letras y números")
    private String password;

    @Schema(description = "Roles del usuario")
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);
}
