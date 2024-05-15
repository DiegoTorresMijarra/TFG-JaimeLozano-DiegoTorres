package es.jaimelozanodiegotorres.backapp.rest.commons.mapper;



/**
 * CommonMapper es una clase abstracta que define métodos para mapear
 * DTOs (Data Transfer Objects) a modelos de entidades para operaciones
 * de guardado y actualización.
 *
 * @param <M> el tipo del modelo de entidad.
 * @param <S> el tipo del DTO utilizado para la operación de guardado.
 * @param <U> el tipo del DTO utilizado para la operación de actualización.
 */
public interface CommonMapper<M, S, U> {

    /**
     * Convierte un DTO de guardado a un modelo de entidad.
     *
     * @param saveDto el DTO de guardado.
     * @return el modelo de entidad.
     */
    abstract M saveToModel(S saveDto);

    /**
     * Convierte un DTO de actualización a un modelo de entidad.
     *
     * @param updateDto el DTO de actualización.
     * @return el modelo de entidad.
     */
    abstract M updateToModel(M original, U updateDto);
}
