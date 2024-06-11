package es.jaimelozanodiegotorres.backapp.rest.user.dto;

import es.jaimelozanodiegotorres.backapp.rest.addresses.models.Addresses;
import es.jaimelozanodiegotorres.backapp.rest.orders.models.Order;
import es.jaimelozanodiegotorres.backapp.rest.user.models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    @Schema(description = "Nombre del usuario", example = "John")
    private String name;

    @Schema(description = "Apellidos del usuario", example = "Doe")
    private String surname;

    @Schema(description = "Username del usuario", example = "johndoe")
    private String username;

    @Schema(description = "Email del usuario", example = "johndoe@localhost")
    private String email;

    @Schema(description = "Roles del usuario")
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);

    @Schema(description = "Pedidos del usuario")
    List<Order> orders;

    @Schema(description = "Direcciones del usuario")
    private List<Addresses> addresses;
}
