package es.jaimelozanodiegotorres.backapp.rest.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {
    @Schema(description = "Identificador de la categoria", example = "1")
    private Long id;
    @Schema(description = "Nombre de la categoria", example = "Postre")
    private String name;
    @Schema(description = "Fecha borrado de la categoria", example = "2022-01-01T00:00:00")
    private LocalDateTime deletedAt;
    @Schema(description = "Fecha de creacion de la categoria", example = "2022-01-01T00:00:00")
    private LocalDateTime createdAt;
    @Schema(description = "Fecha de actualizacion de la categoria", example = "2022-01-01T00:00:00")
    private LocalDateTime updatedAt;
}
