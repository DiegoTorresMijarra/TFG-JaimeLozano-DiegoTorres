package es.jaimelozanodiegotorres.backapp.rest.category.exceptions;

public abstract class CategoryException extends RuntimeException {
    public CategoryException(String message) {
        super(message);
    }
}
