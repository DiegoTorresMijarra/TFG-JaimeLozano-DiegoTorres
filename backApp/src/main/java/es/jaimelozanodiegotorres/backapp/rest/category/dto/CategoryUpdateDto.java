package es.jaimelozanodiegotorres.backapp.rest.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre de la categoria", example = "Gerente")
    private String name;
}
