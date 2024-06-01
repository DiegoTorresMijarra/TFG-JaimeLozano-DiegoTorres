package es.jaimelozanodiegotorres.backapp.rest.evaluation.mappers;

import es.jaimelozanodiegotorres.backapp.rest.commons.mapper.CommonMapper;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationResponseDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.models.Evaluation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EvaluationResponseMapper extends CommonMapper<Evaluation, EvaluationResponseDto> {
    EvaluationResponseMapper INSTANCE = Mappers.getMapper( EvaluationResponseMapper.class );


    @Override
    @Mapping(target = "userName", source = "model.user.name")
    EvaluationResponseDto modelToDto(Evaluation model);
}
