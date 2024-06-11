package es.jaimelozanodiegotorres.backapp.rest.auth.exceptions;

/**
 * Excepción de autenticación
 */
public abstract class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}