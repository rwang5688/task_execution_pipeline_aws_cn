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

        reportXmlFilePath = args[0];
        System.out.println("reportXmlFilePath=" + reportXmlFilePath);

        csvFilePath = args[1];
        System.out.println("csvFilePath=" + csvFilePath);

        String[] csvFilePathElements = csvFilePath.split("\\.");
        System.out.println("csvFilePathElements=" + Arrays.toString(csvFilePathElements));
        String csvFileBase = csvFilePathElements[0];
        pdfFilePath = csvFileBase + ".pdf";
        System.out.println("pdfFilePath=" + pdfFilePath);

        CSVFile csvFile = new CSVFile();
        List<String[]> csvData = csvFile.readData(csvFilePath);
        for (String[] row : csvData) {
            System.out.println("row: " + Arrays.toString(row));
        }

        //String[] columnNames = csvData.get(0);
        String[] columnNames = new String[]{"Id", "Name", "Price"};
        System.out.println("columnNames: " + Arrays.toString(columnNames));
        csvFile.writePDFFile(reportXmlFilePath,
                            columnNames,
                            csvFilePath,
                            pdfFilePath);

        System.out.println("... End csv-to-pdf");
    }
}

