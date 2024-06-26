package es.jaimelozanodiegotorres.backapp.rest.auth.repository;

import es.jaimelozanodiegotorres.backapp.rest.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la autenticación de usuarios
 */
@Repository
public interface AuthUsersRepository extends JpaRepository<User, Long> {
    /**
     * Buscar usuario por username
     * @param username nombre de usuario
     * @return usuario
     */
    Optional<User> findByUsername(String username);
}
