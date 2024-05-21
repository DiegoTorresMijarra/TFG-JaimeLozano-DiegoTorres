package es.jaimelozanodiegotorres.backapp.rest.commons.controller;


import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.commons.filters.CommonFilters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CommonController <M, ID extends Serializable, D> {

    public abstract List<M> listAll();

  //  public abstract ResponseEntity<PageResponse<M>> pageAll(CommonFilters<M> filters);

    public abstract ResponseEntity<M> findById(ID id);

    public abstract ResponseEntity<M> save(D dto);

    public abstract ResponseEntity<M> update(ID id, D dto);

    public abstract ResponseEntity<Boolean> deleteById(ID id);


    /**
     * Manejador de excepciones para los errores de validación
     *
     * @param ex Excepción de validación
     * @return Mapa con los errores de validación
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
