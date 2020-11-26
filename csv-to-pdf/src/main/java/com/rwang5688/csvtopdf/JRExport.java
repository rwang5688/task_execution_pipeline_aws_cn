package com.rwang5688.csvtopdf;

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
            // assume column names are the same for report defn and CSV file header
            JasperReport report = JasperCompileManager.compileReport(xmlFilePath);
            JRCsvDataSource csvDataSource = new JRCsvDataSource(csvFilePath);

            // get column names from CSV file header and set as CSV data source header
            String[] columnNames = getColumnNames(csvFilePath);
            csvDataSource.setColumnNames(columnNames);
            Map<String, Integer> colNames = csvDataSource.getColumnNames();
            System.out.println("JRExport.exportPDFFile: Column names: " +
                                colNames.toString());

            // fill report: ski first row because it's header
            HashMap<String, Object> params = new HashMap<String, Object>();
            csvDataSource.setUseFirstRowAsHeader(true);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, csvDataSource);

            // export filled report to PDF file
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFilePath);
        } catch (JRException e) {
            System.out.println("JRExport.exportPDFFile: " + e);
        }
    }
}

