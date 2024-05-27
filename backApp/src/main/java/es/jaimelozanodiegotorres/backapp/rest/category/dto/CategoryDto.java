package es.jaimelozanodiegotorres.backapp.rest.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private static final String REGEXP_SANITIZER = "^[\\d\\p{L}\\s,.;:]*$";

    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre de la categoria", example = "Gerente")
    @Pattern(regexp = REGEXP_SANITIZER, message = "El campo solo puede contener letras y números")
    private String name;
}
