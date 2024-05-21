package es.jaimelozanodiegotorres.backapp.rest.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ExceptionService {

    private static final String BASE_MESSAGE = "Excepcion durante la ejecucion del servicio. Motivo: ";
    private final String entityName;

    public ExceptionService(String entityName) {
        this.entityName = entityName;
    }

    public EntityNotFoundException notFoundException(String id){
        String message = BASE_MESSAGE + "La entidad " + entityName + " con id "+ id + " No se ha encontrado";
        return new EntityNotFoundException(message);
    }
    public EntityNotFoundException notFoundExceptionName(String name){
        String message = BASE_MESSAGE + "La entidad " + entityName + " con nombre "+ name + " No se ha encontrado";
        return new EntityNotFoundException(message);
    }

    public EntityBadRequestException badRequestException(String msg) {
        String message = BASE_MESSAGE + "Bad Request durante la peticion. Message: " + msg;
        return new EntityBadRequestException(message);
    }

}
