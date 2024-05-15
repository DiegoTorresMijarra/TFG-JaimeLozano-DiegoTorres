package es.jaimelozanodiegotorres.backapp.rest.category.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class CategoryNotFound extends CategoryException {
    public CategoryNotFound(Long id){
        super("No se puede encontrar la Categoria con el id" + id);
    }
    public CategoryNotFound(String name){
        super("No se puede encontrar la Categoria con el nombre" + name);
    }
}
