package com.cd.incidenciasappfx.helper;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.InputStream;
import java.util.List;
import java.util.Map;


/**
 * JasperReportHelper.java
 *
 * @author CDAA
 */
public class JasperReportHelper {

    public static void generateReport(String reportPath, String outputPath, Map<String, Object> params, List<?> data, ReportExporter exporter) {
        try {
            // Cargar y compilar el reporte
            InputStream reportStream = JasperReportHelper.class.getResourceAsStream(reportPath);
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Fuente de datos
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

            // Llenar reporte
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            // Exportar reporte usando el exportador pasado como parámetro
            exporter.export(jasperPrint, outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
