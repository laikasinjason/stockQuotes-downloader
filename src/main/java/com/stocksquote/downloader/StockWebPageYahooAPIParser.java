package com.stocksquote.downloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class StockWebPageYahooAPIParser {
    private Logger log = LoggerFactory.getLogger(StockWebPageYahooAPIParser.class);
    private String pathDirectory;
    private String stockCode;
    private String[] stockCodes;
    private int yearOfRecord;
    private Interval interval;

    public StockWebPageYahooAPIParser(String stockCode, String pathDirectory, int yearOfRecord, Interval interval) {
        this.stockCode = stockCode;
        this.yearOfRecord = yearOfRecord;
        this.interval = interval;
        this.pathDirectory = pathDirectory;
    }

    public StockWebPageYahooAPIParser(String[] stockCodes, String pathDirectory, int yearOfRecord, Interval interval) {
        this.stockCodes = stockCodes;
        this.yearOfRecord = yearOfRecord;
        this.interval = interval;
        this.pathDirectory = pathDirectory;
    }


    public boolean parse() {
        try {
            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();
            from.add(Calendar.YEAR, -yearOfRecord);

            if (stockCode != null) {
                Stock stock = YahooFinance.get(stockCode, from, to, interval);
                log.info("Loaded stock {}", stock.toString());
                saveToCsv(stockCode, stock);

                return true;
            }

            if (stockCodes != null) {
                Map<String, Stock> stockMap = YahooFinance.get(stockCodes, from, to, interval);
                log.info("Loaded stock {}", stockMap.keySet());

                stockMap.forEach((ric, stock) -> {
                    saveToCsv(ric, stock);
                });

                return true;
            }


            log.warn("stockCode not set");
            // return fail result
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void saveToCsv(String stockCode, Stock stock) {

        String csvFileToSave = pathDirectory.concat(stockCode).concat(".csv");

        log.info("Saving file {}", csvFileToSave);
        try {
            CsvWriter csvWriter = new CsvWriter(csvFileToSave);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            csvWriter.writeRawString("Date,RIC,Open,High,Low,Close,Adj Close,Volume");
            List<HistoricalQuote> historicalQuotes = stock.getHistory();
            for (HistoricalQuote historicalQuote : historicalQuotes) {
                List<String> strLine = new ArrayList<>();
                strLine.add(dateFormat.format(historicalQuote.getDate().getTime()));
                strLine.add(historicalQuote.getSymbol());
                strLine.add(historicalQuote.getOpen().toString());
                strLine.add(historicalQuote.getHigh().toString());
                strLine.add(historicalQuote.getLow().toString());
                strLine.add(historicalQuote.getClose().toString());
                strLine.add(historicalQuote.getAdjClose().toString());
                strLine.add(historicalQuote.getVolume().toString());

                csvWriter.write(strLine);
            }


            csvWriter.flushAndClose();

        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Done saving file {}", csvFileToSave);

    }
}
