
package com.cd.incidenciasappfx.helper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * ExcelReportHelper.java
 * 
 * @author CDAA
 */

public class ExcelReportHelper {

    public static void generateExcelReport(String filePath, List<String[]> data, String sheetName) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        // Crear filas y celdas
        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i);
            String[] rowData = data.get(i);
            for (int j = 0; j < rowData.length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(rowData[j]);
            }
        }

        // Guardar archivo
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            System.out.println("Reporte Excel generado en: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
