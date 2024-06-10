package es.jaimelozanodiegotorres.backapp.rest.excel;

public class ExcelException extends RuntimeException{
    public ExcelException(String message) {
        super("Excepcion en el servicio durante la generacion del excel: " + message);
    }
}
