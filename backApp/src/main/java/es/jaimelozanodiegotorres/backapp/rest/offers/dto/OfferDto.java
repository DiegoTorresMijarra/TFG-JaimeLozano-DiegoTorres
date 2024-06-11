package es.jaimelozanodiegotorres.backapp.rest.offers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferDto {
    @PositiveOrZero(message = "La oferta no puede ser negativa")
    @NotNull(message = "La oferta no puede estar vacía")
    @Max(value = 100)
    @Schema(description = "oferta del producto", example = "30")
    private double descuento;

    @Schema(description = "Id del producto", example = "1")
    @NotNull(message="El id no puede estar vacío")
    @Positive(message = "El id no puede ser negativo o 0")
    private Long productId;

    @Schema(description = "Fecha de inicio de la oferta", example = "2022-01-01 00:00:00")
    @NotNull(message="La fecha de inicio no puede estar vacia")
    private LocalDateTime fechaDesde;

    @Schema(description = "Fecha de fin de la oferta", example = "2022-01-01 00:00:00")
    @NotNull(message="La fecha de fin no puede estar vacia")
    private LocalDateTime fechaHasta;
}
