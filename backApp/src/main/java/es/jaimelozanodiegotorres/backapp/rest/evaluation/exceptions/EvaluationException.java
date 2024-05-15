package es.jaimelozanodiegotorres.backapp.rest.evaluation.exceptions;

public abstract  class EvaluationException extends RuntimeException {
    public EvaluationException(String message) {
        super(message);
    }
}

