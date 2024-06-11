package es.jaimelozanodiegotorres.backapp.rest.auth.service.authentication;


import es.jaimelozanodiegotorres.backapp.rest.auth.dto.JwtAuthResponse;
import es.jaimelozanodiegotorres.backapp.rest.auth.dto.UserSignInRequest;
import es.jaimelozanodiegotorres.backapp.rest.auth.dto.UserSignUpRequest;

/**
 * Interfdaz servicio de autenticación
 */
public interface AuthenticationService {
    JwtAuthResponse signUp(UserSignUpRequest request);

    JwtAuthResponse signIn(UserSignInRequest request);
}