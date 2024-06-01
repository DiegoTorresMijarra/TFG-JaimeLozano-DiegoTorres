package es.jaimelozanodiegotorres.backapp.rest.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationResponseDto {

    @Schema(description = "Valoracion del producto", example = "5")
    private Integer value;

    @Schema(description = "Id del producto", example = "1")
    private long productId;

    @Schema(description = "Comentario del producto", example = "Muy bueno un 10")
    private String comment;

    @Schema(description = "Usuario de la valoracion")
    private String userName;
}
