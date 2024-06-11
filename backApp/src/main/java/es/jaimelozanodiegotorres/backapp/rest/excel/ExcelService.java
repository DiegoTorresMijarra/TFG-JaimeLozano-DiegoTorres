package es.jaimelozanodiegotorres.backapp.rest.excel;

import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;

public interface ExcelService {

    void generateExcelOrderById(ObjectId id, HttpServletResponse response) throws ExcelException;
}
