package com.stocksquote.downloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvWriter {
    private Logger log = LoggerFactory.getLogger(CsvWriter.class);
    private FileWriter writer;
    private String csvFileName;

    public CsvWriter(String csvFile) throws IOException {
        this.writer = new FileWriter(csvFile);
        this.csvFileName = csvFile.substring(csvFile.lastIndexOf("/") + 1);
    }

    public void write(List<String> strLine) throws IOException {
        log.info("Writing line {} to file {}", strLine, csvFileName);

        CsvUtils.writeLine(writer, strLine);
    }

    public void flushAndClose() throws IOException {

        writer.flush();
        writer.close();
    }


}
