package es.jaimelozanodiegotorres.backapp.rest.offers.mapper;

import es.jaimelozanodiegotorres.backapp.rest.commons.mapper.CommonMapper;
import es.jaimelozanodiegotorres.backapp.rest.offers.dto.OfferDto;
import es.jaimelozanodiegotorres.backapp.rest.offers.models.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OfferMapper extends CommonMapper<Offer, OfferDto>{
    OfferMapper INSTANCE = Mappers.getMapper( OfferMapper.class );
}
