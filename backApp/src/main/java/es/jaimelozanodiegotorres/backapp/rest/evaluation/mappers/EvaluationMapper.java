package es.jaimelozanodiegotorres.backapp.rest.evaluation.mappers;

import es.jaimelozanodiegotorres.backapp.rest.commons.mapper.CommonMapper;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.dto.EvaluationDto;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.models.Evaluation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EvaluationMapper extends CommonMapper<Evaluation, EvaluationDto> {
    EvaluationMapper INSTANCE = Mappers.getMapper( EvaluationMapper.class );
}
