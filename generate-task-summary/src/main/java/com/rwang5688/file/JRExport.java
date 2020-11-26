package com.rwang5688.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperExportManager;

import java.util.Map;
import java.util.HashMap;


public class JRExport {
    private static final Logger logger = LoggerFactory.getLogger(JRExport.class);

    private String[] getColumnNames(String csvFilePath) {
        String[] columnNames;

        CSVFile csvFile = new CSVFile();
        columnNames = csvFile.readHeader(csvFilePath);

        return columnNames;
    }

    public void exportPDFFile(String xmlFilePath,
                                String csvFilePath,
                                String pdfFilePath) {
        try {
            // debug
            logger.info("JRExport.exportPDFFile: Begin with " +
                            "xmlFilePath=" + xmlFilePath + ", " +
                            "csvFilePath=" + csvFilePath + ", " +
                            "pdfFilePath=" + pdfFilePath + ". ");

            // assume column names are the same for report defn and CSV file header
            JasperReport report = JasperCompileManager.compileReport(xmlFilePath);
            logger.info("JRExport.exportPDFFile: Compiled report.");

            JRCsvDataSource csvDataSource = new JRCsvDataSource(csvFilePath);
            logger.info("JRExport.exportPDFFile: Initialized data source.");

            // get column names from CSV file header and set as CSV data source header
            String[] columnNames = getColumnNames(csvFilePath);
            csvDataSource.setColumnNames(columnNames);
            Map<String, Integer> colNames = csvDataSource.getColumnNames();
            logger.info("JRExport.exportPDFFile: Set column names: " + colNames.toString());

            // fill report: ski first row because it's header
            HashMap<String, Object> params = new HashMap<String, Object>();
            csvDataSource.setUseFirstRowAsHeader(true);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, csvDataSource);
            logger.info("JRExport.exportPDFFile: Filled report.");

            // export filled report to PDF file
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFilePath);
            logger.info("JRExport.exportPDFFile: Exported PDF file.");
        } catch (JRException e) {
            logger.error("JRExport.exportPDFFile: " + e);
        }
    }
}

