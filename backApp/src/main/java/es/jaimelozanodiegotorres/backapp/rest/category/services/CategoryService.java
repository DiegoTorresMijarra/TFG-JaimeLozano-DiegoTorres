package es.jaimelozanodiegotorres.backapp.rest.category.services;

import es.jaimelozanodiegotorres.backapp.rest.category.dto.CategorySaveDto;
import es.jaimelozanodiegotorres.backapp.rest.category.dto.CategoryUpdateDto;
import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {
    /**
     * Obtiene todas las posiciones que coincidan con los parámetros
     * @param name opcional: nombre de la posición
     * @param pageable paginación
     * @return Page de las posiciones que coinciden con los parámetros
     */
    Page<Category> findAll(Optional<String> name, Pageable pageable);

    /**
     * Obtiene la posición con el id dado
     * @param id id de la posición a obtener
     * @return Posicion con ese id
     */
    Category findById(Long id);

    Category findByName(String name);

    /**
     * Guarda una posición
     * @param category posición a guardar
     * @return posición guardada
     */
    Category save(CategorySaveDto category);

    /**
     * Actualiza una posición
     * @param id id de la posición a actualizar
     * @param category posición con los datos a actualizar
     * @return posición actualizada
     */
    Category update(Long id, CategoryUpdateDto category);

    /**
     * Elimina una posición
     * @param id id de la posición a eliminar
     */
    void deleteById(Long id);
}
