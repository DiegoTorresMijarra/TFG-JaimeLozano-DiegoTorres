package es.jaimelozanodiegotorres.backapp.rest.user.repository;

import es.jaimelozanodiegotorres.backapp.rest.commons.repository.CommonRepository;
import es.jaimelozanodiegotorres.backapp.rest.user.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CommonRepository<User, UUID>{
    @Override
    default String getTableName(){
        return "usuarios";
    }

    /**
     * Devuelve un optional de un usuario por su nombre de usuario
     * @param username ID del usuario a buscar
     * @return Optional de un usuario
     */
    Optional<User> findByUsername(String username);

    /**
     * Devuelve un optional de un usuario por su email
     * @param email email del usuario buscado
     * @return Optional de un usuario
     */
    Optional<User> findByEmail(String email);

    /**
     * Devuelve un optional de un usuario ignorando mayúsculas y minúsculas a partir de su nombre de usuario
     * @param username nombre del usuario buscado
     * @return Optional de un usuario
     */
    Optional<User> findByUsernameEqualsIgnoreCase(String username);
    /**
     * Devuelve un optional de un usuario ignorando mayúsculas y minúsculas a partir de su email
     * @param email email del usuario buscado
     * @return Optional de un usuario
     */
    Optional<User> findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(String username, String email);



    /**
     * Devuelve una lista de usuarios que contengan en su nombre de usuario el parámetro pasado
     * ignorando mayúsculas y minúsculas
     * @param username nombre del usuario a buscar
     * @return Lista de usuarios
     */
    List<User> findAllByUsernameContainingIgnoreCase(String username);

    /**
     * Hace un borrado logico de un usuario por su ID
     * cambiando el campo isDeleted a true
     * @param id ID del usuario a borrar
     */
    @Modifying // Para indicar que es una consulta de actualización
    @Query("UPDATE User p SET p.deletedAt = true WHERE p.id = :id")
    void updateIsDeletedToTrueById(UUID id);
}
