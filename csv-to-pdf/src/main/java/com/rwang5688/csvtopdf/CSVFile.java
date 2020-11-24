package com.rwang5688.csvtopdf;

import java.io.FileReader;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.util.List;
import java.util.ArrayList;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperExportManager;
import java.util.HashMap;


public class CSVFile {
    public List<String[]> readData(String filePath) {
        List<String[]> csvData = new ArrayList<String[]>();

	    try {
		    FileReader fileReader = new FileReader(filePath);

		    // create csvReader object and skip first Line
		    CSVReader csvReader = new CSVReaderBuilder(fileReader)
								        .withSkipLines(1)
								        .build();
		    csvData = csvReader.readAll();
	    }
	    catch (Exception e) {
		    System.out.println("CSVFile.readData: " + e);
        }

        return csvData;
	}

	public void writePDFFile(String reportXmlFilePath,
						String[] columnNames,
						String csvFilePath,
						String pdfFilePath) {
		try {
			JasperReport report = JasperCompileManager.compileReport(reportXmlFilePath);

			JRCsvDataSource csvDataSource = new JRCsvDataSource(csvFilePath);
			csvDataSource.setColumnNames(columnNames);

			HashMap<String, Object> params = new HashMap<String, Object>();
			JasperPrint jasperPrint = JasperFillManager.fillReport(report,
																params,
																csvDataSource);

			JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFilePath);
		} catch (JRException e) {
			System.out.println("CSVFile.convertToPDF: " + e);
		}
    }
}

