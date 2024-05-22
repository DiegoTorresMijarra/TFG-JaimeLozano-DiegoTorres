package es.jaimelozanodiegotorres.backapp.rest.evaluation.filters;

import es.jaimelozanodiegotorres.backapp.rest.commons.filters.CommonFilters;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.models.Evaluation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationFilters extends CommonFilters<Evaluation>{

    @Builder.Default
    private Optional<Integer> ValorMax = Optional.empty();

    @Builder.Default
    private Optional<Integer> ValorMin = Optional.empty();

    @Override
    public Specification<Evaluation> getSpecifications() {
        //Criterio por valoracion maximo
        Specification<Evaluation> specValorMax= (root, query, criteriaBuilder) ->
                ValorMax.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("valoracion"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        //Criterio por valoracion minimo
        Specification<Evaluation> specValorMin= (root, query, criteriaBuilder) ->
                ValorMin.map(p -> criteriaBuilder.greaterThanOrEqualTo(root.get("valoracion"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        return Specification.where(specValorMax).and(specValorMin);
    }
}
