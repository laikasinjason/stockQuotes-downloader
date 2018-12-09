package com.stocksquote.downloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class CsvReader {
    private Logger log = LoggerFactory.getLogger(CsvReader.class);

    String csvFile = "stocksQuotes.csv";
    BufferedReader br = null;
    String line = "";
    String cvsSplitBy = ",";


    public String[] readCsv(String csvFile) {
        try {
            //Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(csvFile).getFile());
            FileReader fileReader = new FileReader(file);
            br = new BufferedReader(fileReader);

            line = br.readLine();
            // use comma as separator
            String[] stocks = line.split(cvsSplitBy);

            return stocks;

        } catch (FileNotFoundException e) {
            log.error("File {} not found.", csvFile);
            return null;
        } catch (IOException e) {
            log.error("Error occurs on reading {}.", csvFile);
        } finally {
            if (br != null) try {
                br.close();
            } catch (IOException e) {
                log.error("Error occurs on reading {}.", csvFile);
            }
        }
        return null;
    }
}
