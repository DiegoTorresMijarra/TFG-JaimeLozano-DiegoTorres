package es.jaimelozanodiegotorres.backapp.rest.evaluation.mapper;

import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationResponseDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationSaveDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationUpdateDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.models.Evaluation;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;

public class EvaluationMapper {
    /**
     * Mapea los datos del dto a la entidad
     * @param evaluationSaveDto   dto con los datos del trabajador
     * @param product        posición del trabajador
     * @return entidad creada con los datos del dto
     */
    public static Evaluation toModel(EvaluationSaveDto evaluationSaveDto, Product product) {
        return Evaluation.builder()
                .product(product)
                .valoracion(evaluationSaveDto.getValoracion())
                .build();
    }

    /**
     * Mapea los datos del dto a la entidad
     * @param evaluationOriginal  entidad original
     * @param evaluationUpdateDto   dto con los datos a actualizar
     * @param product       posición del trabajador
     * @return entidad actualizada con los datos del dto
     */
    public static Evaluation toModel(Evaluation evaluationOriginal, EvaluationUpdateDto evaluationUpdateDto, Product product) {
        return Evaluation.builder()
                .id(evaluationOriginal.getId())
                .valoracion(evaluationUpdateDto.getValoracion())
                .product(product)
                .createdAt(evaluationOriginal.getCreatedAt())
                .build();
    }

    /**
     * Mapea los datos de la entidad a la respuesta
     * @param evaluation entidad con los datos del trabajador
     * @return dto con los datos de la entidad
     */
    public static EvaluationResponseDto toWorkersResponseDto(Evaluation evaluation){
        return EvaluationResponseDto.builder()
                .id(evaluation.getId())
                .valoracion(evaluation.getValoracion())
                .updatedAt(evaluation.getUpdatedAt())
                .createdAt(evaluation.getCreatedAt())
                .deletedAt(evaluation.getDeletedAt())
                .product(evaluation.getProduct())
                .build();
    }
}
