package es.jaimelozanodiegotorres.backapp.rest.evaluation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class EvaluationNotFound extends EvaluationException {
    public EvaluationNotFound(Long id){
        super("No se puede encontrar la Valoracion con el id" + id);
    }
}
