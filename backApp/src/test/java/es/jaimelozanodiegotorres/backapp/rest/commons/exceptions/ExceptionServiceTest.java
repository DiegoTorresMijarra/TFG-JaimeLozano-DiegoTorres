package es.jaimelozanodiegotorres.backapp.rest.commons.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExceptionServiceTest {

    private static final String ENTITY_NAME = "TestEntity";
    private ExceptionService exceptionService;

    @BeforeEach
    void setUp() {
        exceptionService = new ExceptionService(ENTITY_NAME);
    }

    @Test
    void notFoundException_ById() {
        String id = "123";
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            throw exceptionService.notFoundException(id);
        });
        assertEquals("Excepcion durante la ejecucion del servicio. Motivo: La entidad TestEntity con id 123 No se ha encontrado", exception.getMessage());
    }

    @Test
    void notFoundException_ByName() {
        String name = "TestName";
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            throw exceptionService.notFoundExceptionName(name);
        });
        assertEquals("Excepcion durante la ejecucion del servicio. Motivo: La entidad TestEntity con nombre TestName No se ha encontrado", exception.getMessage());
    }

    @Test
    void badRequestException_WithMessage() {
        String customMessage = "Invalid data provided";
        EntityBadRequestException exception = assertThrows(EntityBadRequestException.class, () -> {
            throw exceptionService.badRequestException(customMessage);
        });
        assertEquals("Excepcion durante la ejecucion del servicio. Motivo: Bad Request durante la peticion. Message: Invalid data provided", exception.getMessage());
    }

    @Test
    void badRequestException_WithoutMessage() {
        EntityBadRequestException exception = assertThrows(EntityBadRequestException.class, () -> {
            throw exceptionService.badRequestException();
        });
        assertEquals("Ups, ha pasado algo raro. Prueba más tarde.", exception.getMessage());
    }
}
