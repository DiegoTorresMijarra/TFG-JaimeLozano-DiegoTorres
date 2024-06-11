package es.jaimelozanodiegotorres.backapp.rest.user.filters;

import es.jaimelozanodiegotorres.backapp.rest.commons.filters.CommonFilters;
import es.jaimelozanodiegotorres.backapp.rest.evaluation.models.Evaluation;
import es.jaimelozanodiegotorres.backapp.rest.user.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFilters extends CommonFilters<User>{

    @Builder.Default
    private Optional<String> username = Optional.empty();
    @Builder.Default
    private Optional<String> email = Optional.empty();
    @Builder.Default
    private Optional<Boolean> isDeleted = Optional.empty();

    @Override
    public Specification<User> getSpecifications() {
        // Criterio de búsqueda por nombre
        Specification<User> specUsernameUser = (root, query, criteriaBuilder) ->
                username.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Criterio de búsqueda por email
        Specification<User> specEmailUser = (root, query, criteriaBuilder) ->
                email.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Criterio de búsqueda por borrado
        Specification<User> specIsDeleted = (root, query, criteriaBuilder) ->
                isDeleted.map(m -> criteriaBuilder.equal(root.get("isDeleted"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

       return Specification.where(specUsernameUser)
                .and(specEmailUser)
                .and(specIsDeleted);
    }
}
