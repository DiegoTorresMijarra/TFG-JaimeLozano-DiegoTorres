package es.jaimelozanodiegotorres.backapp.rest.excel;

import es.jaimelozanodiegotorres.backapp.rest.commons.services.CommonService;
import es.jaimelozanodiegotorres.backapp.rest.orders.models.Order;
import es.jaimelozanodiegotorres.backapp.rest.orders.models.OrderedProduct;
import es.jaimelozanodiegotorres.backapp.rest.orders.service.OrderService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ExcelServiceImpl extends CommonService implements ExcelService {

    private final OrderService orderService;

    private final Field[] orderFields = Order.class.getDeclaredFields();
    private final Field[] orderedProductFields = OrderedProduct.class.getDeclaredFields();

    private final int defaultWidth = 32 * 256;

    @Autowired
    public ExcelServiceImpl(OrderService orderService) {
        super("Excel");
        this.orderService = orderService;
    }

    /**
     * Busca la order, genera el workbook para el excel y llama al metodo para escribir la order. Despues, la escribe en la respuesta.
     * @param id de la order que crea el excel.
     * @param response respuesta a devolver al servidor.
     * @throws ExcelException si ocurre un error a la hora de acceder/escribir a los recursos.
     */
    @Override
    public void generateExcelOrderById(ObjectId id, HttpServletResponse response) throws ExcelException{
        log.info("Generando excel de la order con id: {}", id);
        Order order = orderService.findById(id);

        if(!verifyWorker())
            verifyLogguedSameUser(order.getUserId());

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Order_" + id); // TODO: tal vez, interese poner el nombre

        try {
            writeOrder(workbook, sheet, order);
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (IOException | IllegalAccessException e) {
            throw new ExcelException(e.getMessage());
        }
    }

    /**
     * crea las columnas que contienen el header y el content de la order. Llama a los metodos que generan los estilos.
     * Tras esto itera sobre los campos de la order y pinta los resultados en el excel
     * @param workbook workbook del excel
     * @param sheet hoja  del excel
     * @param order order a pintar
     * @throws IllegalAccessException si no puede acceder a algun atributo de la order.
     */
    private void writeOrder(XSSFWorkbook workbook, XSSFSheet sheet, Order order) throws IllegalAccessException {
        Row row = sheet.createRow(0);
        Row row1 = sheet.createRow(1);

        CellStyle headerStyle = getHeaderStyle(workbook); // estilos de celda en funcion
        CellStyle contentStyle = getContentStyle(workbook);
        CellStyle nullStyle = getNullStyle(workbook);


        int columnCount = 0;
        for( Field field : orderFields){
            try {
                field.setAccessible(true);
                if(field.getName().equals("orderedProducts")){
                    writeOrderedProducts(workbook, sheet, (List)field.get(order));
                    continue;
                }
                createCell(row, columnCount, field.getName().toUpperCase(), headerStyle, nullStyle);
                createCell(row1, columnCount, field.get(order), contentStyle, nullStyle);
                //sheet.setColumnWidth(columnCount, defaultWidt);
                sheet.autoSizeColumn(columnCount);
                columnCount++;
            } finally {
                field.setAccessible(false);
            }
        }
    }

    private void writeOrderedProducts(XSSFWorkbook workbook, XSSFSheet sheet, List<OrderedProduct> orderedProducts) throws IllegalAccessException {
        int rowCount = 4;

        Row headerRow = sheet.createRow(rowCount++);
        CellStyle headerStyle = getHeaderStyle(workbook);
        CellStyle contentStyle = getContentStyle(workbook);
        CellStyle nullStyle = getNullStyle(workbook);

        int columnCount = 0;
        for (Field field : orderedProductFields) {
            field.setAccessible(true);
            createCell(headerRow, columnCount++, field.getName().toUpperCase(), headerStyle, nullStyle);
            field.setAccessible(false);
        }

        for (OrderedProduct orderedProduct : orderedProducts) {
            Row productRow = sheet.createRow(rowCount++);
            columnCount = 0;
            for (Field field : orderedProductFields) {
                field.setAccessible(true);
                createCell(productRow, columnCount++, field.get(orderedProduct), contentStyle, nullStyle);
                sheet.autoSizeColumn(columnCount);
                field.setAccessible(false);
            }
        }
    }


    /**
     * Pinta el valor en la celda y aplica los estilos y formatos en funcion del valor pasado.
     * @param row fila donde se pinta
     * @param columnCount columna donde se pinta
     * @param valueOfCell valor a pintar
     * @param style estilo de la celda
     * @param nullStyle estilo de la celda con valor null
     */
    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style, CellStyle nullStyle) {
        Cell cell = row.createCell(columnCount);
        if (valueOfCell != null){
            if (valueOfCell instanceof Integer) {
                cell.setCellValue((Integer) valueOfCell);
            } else if (valueOfCell instanceof Long) {
                cell.setCellValue((Long) valueOfCell);
            } else if (valueOfCell instanceof String) {
                cell.setCellValue((String) valueOfCell);
            } else if (valueOfCell instanceof Boolean){
                cell.setCellValue((Boolean)valueOfCell ? "Si": "No");
            } else if(valueOfCell instanceof Set) {
                cell.setCellValue(((Set<?>) valueOfCell).size());
            } else {
                cell.setCellValue(valueOfCell.toString());
            }

            cell.setCellStyle(style);
        } else {
            cell.setCellValue("null");
            cell.setCellStyle(nullStyle);
        }


    }

    private CellStyle getHeaderStyle(XSSFWorkbook workbook){
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);

        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(12);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        return style;
    }

    private CellStyle getContentStyle(XSSFWorkbook workbook){
        CellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        XSSFFont font = workbook.createFont();
        font.setFontHeight(12);
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);

        return style;
    }

    private CellStyle getNullStyle(XSSFWorkbook workbook){
        CellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        XSSFFont font = workbook.createFont();
        font.setFontHeight(12);
        font.setColor(IndexedColors.RED.getIndex());
        style.setFont(font);

        return style;
    }
}
