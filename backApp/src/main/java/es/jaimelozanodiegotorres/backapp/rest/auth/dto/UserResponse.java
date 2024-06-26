package es.jaimelozanodiegotorres.backapp.rest.auth.dto;

import es.jaimelozanodiegotorres.backapp.rest.user.models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Respuesta de autenticación de usuario
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    @Schema(description = "User id", example = "1")
    private Long id;
    @Schema(description = "User name", example = "John")
    private String name;
    @Schema(description = "User last name", example = "Doe")
    private String surname;
    @Schema(description = "User username", example = "johndoe")
    private String username;
    @Schema(description = "User email", example = "johndoe@my.com")
    private String email;
    @Schema(description = "User roles", example = "USER")
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);
}
