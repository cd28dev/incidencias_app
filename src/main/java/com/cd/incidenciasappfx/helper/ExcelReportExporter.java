package com.cd.incidenciasappfx.helper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 * ExcelReportExporter.java
 *
 * @author CDAA
 */
public class ExcelReportExporter implements ReportExporter {

    @Override
    public void export(JasperPrint jasperPrint, String outputPath) throws JRException {
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputPath));

        SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
        config.setOnePagePerSheet(true);
        config.setRemoveEmptySpaceBetweenRows(true);
        exporter.setConfiguration(config);

        exporter.exportReport();
    }

}
