package es.jaimelozanodiegotorres.backapp.rest.auth.service.authentication;

import es.jaimelozanodiegotorres.backapp.rest.auth.dto.JwtAuthResponse;
import es.jaimelozanodiegotorres.backapp.rest.auth.dto.UserSignInRequest;
import es.jaimelozanodiegotorres.backapp.rest.auth.dto.UserSignUpRequest;
import es.jaimelozanodiegotorres.backapp.rest.auth.exceptions.AuthSingInInvalid;
import es.jaimelozanodiegotorres.backapp.rest.auth.exceptions.UserAuthNameOrEmailExisten;
import es.jaimelozanodiegotorres.backapp.rest.auth.exceptions.UserDiferentePasswords;
import es.jaimelozanodiegotorres.backapp.rest.auth.repository.AuthUsersRepository;
import es.jaimelozanodiegotorres.backapp.rest.auth.service.jwt.JwtService;
import es.jaimelozanodiegotorres.backapp.rest.user.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private AuthUsersRepository authUsersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    public void testSignUp_WhenPasswordsMatch_ShouldReturnToken() {
        // Datos de prueba
        UserSignUpRequest request = new UserSignUpRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setPasswordRepeat("password");
        request.setEmail("test@example.com");
        request.setName("Test");
        request.setSurname("User");

        // Mock del repositorio de usuarios
        User userStored = new User();
        when(authUsersRepository.save(any(User.class))).thenReturn(userStored);

        // Mock del servicio JWT
        String token = "test_token";
        when(jwtService.generateToken(userStored)).thenReturn(token);

        // Llamada al método a probar
        JwtAuthResponse response = authenticationService.signUp(request);

        // Verificaciones
        assertAll("Sign Up",
                () -> assertNotNull(response),
                () -> assertEquals(token, response.getToken()),
                () -> verify(authUsersRepository, times(1)).save(any(User.class)),
                () -> verify(jwtService, times(1)).generateToken(userStored)
        );
    }

    @Test
    public void testSignUp_WhenPasswordsDoNotMatch_ShouldThrowException() {
        // Datos de prueba
        UserSignUpRequest request = new UserSignUpRequest();
        request.setUsername("testuser");
        request.setPassword("password1");
        request.setPasswordRepeat("password2");
        request.setEmail("test@example.com");
        request.setName("Test");
        request.setSurname("User");

        // Llamada al método a probar y verificación de excepción
        assertThrows(UserDiferentePasswords.class, () -> authenticationService.signUp(request));
    }

    @Test
    public void testSignUp_WhenUsernameOrEmailAlreadyExist_ShouldThrowException() {
        // Datos de prueba
        UserSignUpRequest request = new UserSignUpRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setPasswordRepeat("password");
        request.setEmail("test@example.com");
        request.setName("Test");
        request.setSurname("User");

        // Mock del repositorio de usuarios
        when(authUsersRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        // Llamada al método a probar y verificación de excepción
        assertThrows(UserAuthNameOrEmailExisten.class, () -> authenticationService.signUp(request));
    }

    @Test
    public void testSignIn_WhenValidCredentials_ShouldReturnToken() {
        // Datos de prueba
        UserSignInRequest request = new UserSignInRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        // Mock del repositorio de usuarios
        User user = new User();
        when(authUsersRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));

        // Mock del servicio JWT
        String token = "test_token";
        when(jwtService.generateToken(user)).thenReturn(token);

        // Llamada al método a probar
        JwtAuthResponse response = authenticationService.signIn(request);

        // Verificaciones
        assertAll("Sign In",
                () -> assertNotNull(response),
                () -> assertEquals(token, response.getToken()),
                () -> verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class)),
                () -> verify(authUsersRepository, times(1)).findByUsername(request.getUsername()),
                () -> verify(jwtService, times(1)).generateToken(user)
        );
    }

    @Test
    public void testSignIn_WhenInvalidCredentials_ShouldThrowException() {
        // Datos de prueba
        UserSignInRequest request = new UserSignInRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        // Mock del repositorio de usuarios
        when(authUsersRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());

        // Llamada al método a probar y verificación de excepción
        assertThrows(AuthSingInInvalid.class, () -> authenticationService.signIn(request));
    }

}