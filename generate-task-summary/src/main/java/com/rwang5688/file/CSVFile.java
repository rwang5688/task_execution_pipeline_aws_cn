package com.rwang5688.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.util.List;
import java.util.ArrayList;


public class CSVFile {
    private static final Logger logger = LoggerFactory.getLogger(CSVFile.class);

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
		    logger.info("CSVFile.readData: " + e);
        }

        return csvData;
    }
}

