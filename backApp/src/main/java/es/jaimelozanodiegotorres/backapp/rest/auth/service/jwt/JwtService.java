package es.jaimelozanodiegotorres.backapp.rest.auth.service.jwt;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Interfaz servicio de jwt
 */
public interface JwtService {
    String extractUserName(String token);

    String extractUserId(String token);

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);
}
