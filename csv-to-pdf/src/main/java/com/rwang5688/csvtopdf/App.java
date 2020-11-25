package com.rwang5688.csvtopdf;

import java.util.Arrays;
import java.util.List;

/**
 * csv-to-pdf.
 */
public final class App {
    private App() {
    }

    /**
     * main.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        String reportXmlFilePath = "";
        String csvFilePath = "";
        String pdfFilePath = "";

        System.out.println("Start csv-to-pdf ...");

        System.out.println("==");
        System.out.println("Set file paths");
        System.out.println("==");
        reportXmlFilePath = args[0];
        System.out.println("reportXmlFilePath=" + reportXmlFilePath);
        csvFilePath = args[1];
        System.out.println("csvFilePath=" + csvFilePath);
        // set and print PDF file path as $(csvFileBase).pdf
        String[] csvFilePathElements = csvFilePath.split("\\.csv");
        System.out.println("csvFilePathElements=" + Arrays.toString(csvFilePathElements));
        String csvFileBase = csvFilePathElements[0];
        pdfFilePath = csvFileBase + ".pdf";
        System.out.println("pdfFilePath=" + pdfFilePath);

        System.out.println("==");
        System.out.println("Test CSV file reader");
        System.out.println("==");
        CSVFile csvFile = new CSVFile();
        // read the entire CSV file including header (skipHeader=false)
        List<String[]> csvAll = csvFile.readAll(csvFilePath, false);
        for (String[] row : csvAll) {
            System.out.println("row: " + Arrays.toString(row));
        }

        System.out.println("==");
        System.out.println("Test JaperReport PDF file export");
        System.out.println("==");
        JasperReportExport jrExport = new JasperReportExport();
        jrExport.exportPDFFile(reportXmlFilePath,
                            csvFilePath,
                            pdfFilePath);

        System.out.println("... End csv-to-pdf");
    }
}

