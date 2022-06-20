package com.example.crypto.infrastructure.util;

import com.example.crypto.infrastructure.exception.FileNotFoundException;
import com.example.crypto.infrastructure.persistence.CryptoPriceDocument;
import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class FileCryptoReader {

    /**
     * Retrieves the crypto values from the given csv file.
     *
     * @param csvFile    The file to extract the values from.
     * @return A list of CryptoPriceDocument
     */
    public List<CryptoPriceDocument> extractCryptoValuesFromCsv(String csvFile) {
        try {
            InputStream input = getClass().getResourceAsStream("/" + csvFile);
            CSVReader csvReader = new CSVReader(
                    new InputStreamReader(input), CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER,1);
            return csvReader.readAll().stream()
                    .map(this::map)
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            throw new FileNotFoundException();
        }
    }

    /**
     * Converts a String array with fixed elements (0 : timestamp, 1 : symbol, 2 : price) to a CryptoPriceDocument.
     *
     * @param row    A String array.
     * @return A CryptoPriceDocument
     */
    private CryptoPriceDocument map(String[] row)  {

        Timestamp timestamp = new Timestamp(Long.parseLong(row[0]));

        return new CryptoPriceDocument(timestamp, row[1], Double.parseDouble(row[2]));
    }
}
