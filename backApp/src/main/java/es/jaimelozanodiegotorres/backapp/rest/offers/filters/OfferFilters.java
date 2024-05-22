package es.jaimelozanodiegotorres.backapp.rest.offers.filters;

import es.jaimelozanodiegotorres.backapp.rest.commons.filters.CommonFilters;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.models.Evaluation;
import es.jaimelozanodiegotorres.backapp.rest.offers.models.Offer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferFilters extends CommonFilters<Offer>{

    @Builder.Default
    private Optional<Integer> ValorMax = Optional.empty();

    @Builder.Default
    private Optional<Integer> ValorMin = Optional.empty();

    @Override
    public Specification<Offer> getSpecifications() {
        //Criterio por descuento maximo
        Specification<Offer> specValorMax= (root, query, criteriaBuilder) ->
                ValorMax.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("descuento"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        //Criterio por descuento minimo
        Specification<Offer> specValorMin= (root, query, criteriaBuilder) ->
                ValorMin.map(p -> criteriaBuilder.greaterThanOrEqualTo(root.get("descuento"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        return Specification.where(specValorMax).and(specValorMin);
    }
}
