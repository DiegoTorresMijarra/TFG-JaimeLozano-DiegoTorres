package es.jaimelozanodiegotorres.backapp.rest.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ExceptionService {

    private static final String BASE_MESSAGE = "Excepcion durante la ejecucion del servicio. Motivo: ";
    private final String entityName;

    public ExceptionService(String entityName) {
        this.entityName = entityName;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RuntimeException notFoundException(String id){
        String message = BASE_MESSAGE + "La entidad " + entityName + " con id "+ id + " No se ha encontrado";
        return new RuntimeException(message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RuntimeException badRequestException(String msg) {
        String message = BASE_MESSAGE + "Bad Request durante la peticion. Message: " + msg;
        return new RuntimeException(message);
    }

}
