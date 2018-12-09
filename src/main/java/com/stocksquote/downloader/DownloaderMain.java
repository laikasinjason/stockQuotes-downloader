package com.stocksquote.downloader;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yahoofinance.histquotes.Interval;

public class DownloaderMain {

    public static void main(String[] args) {
        Logger log = LoggerFactory.getLogger(DownloaderMain.class);


        int year = 5;
        String pathDirectory = "/media/D/HKDailyStocksQuotes/";



        log.info("Starting stocks quote downloader");

        CsvReader csvReader = new CsvReader();
        String[] stockCodes = csvReader.readCsv("stocksQuotes.csv");

        StockWebPageYahooAPIParser stockWebPageYahooAPIParser = new StockWebPageYahooAPIParser(stockCodes, pathDirectory, year, Interval.DAILY);
        stockWebPageYahooAPIParser.parse();

        log.info("Finish downloading stocks quotes");
    }

}
