package es.jaimelozanodiegotorres.backapp.rest.evaluation.dto;

import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationResponseDto {
    @Schema(description = "Identificador de la categoria", example = "1")
    private Long id;
    @Schema(description = "Valoracion ", example = "Postre")
    private Integer valoracion;
    @Schema(description = "Fecha borrado de la categoria", example = "2022-01-01T00:00:00")
    private LocalDateTime deletedAt;
    @Schema(description = "Fecha de creacion de la categoria", example = "2022-01-01T00:00:00")
    private LocalDateTime createdAt;
    @Schema(description = "Fecha de actualizacion de la categoria", example = "2022-01-01T00:00:00")
    private LocalDateTime updatedAt;
    @Schema(description = "Producto", example = "Hamburguesa")
    private Product product;
}
