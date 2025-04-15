package com.cd.incidenciasappfx.helper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * PdfReportExporter.java
 *
 * @author CDAA
 */
public class PdfReportExporter implements ReportExporter {

    @Override
    public void export(JasperPrint jasperPrint, String outputPath) throws JRException {
        JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
        System.out.println("✅ Reporte PDF generado en: " + outputPath);
    }

}
