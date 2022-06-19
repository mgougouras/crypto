package com.example.crypto.infrastructure.persistence;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.crypto.infrastructure.util.FileCryptoReader;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.test.StepVerifier;

@RunWith(MockitoJUnitRunner.class)
public class CryptoDocumentRepositoryTest {

    @Mock
    FileCryptoReader fileReader;

    @InjectMocks
    CryptoDocumentRepositoryImpl repository;

    @Before
    public void setUp() {
        reset(fileReader);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(fileReader);
    }

    // -----------------------------------
    // Tests: findAllByDateRange
    // -----------------------------------
    @Test
    public void test_findAllByDateRange() {

        when(fileReader.extractCryptoValuesFromCsv(eq("BTC_values.csv"))).thenReturn(getCryptoDocuments("BTC"));
        when(fileReader.extractCryptoValuesFromCsv(eq("DOGE_values.csv"))).thenReturn(getCryptoDocuments("DOGE"));
        when(fileReader.extractCryptoValuesFromCsv(eq("ETH_values.csv"))).thenReturn(getCryptoDocuments("ETH"));
        when(fileReader.extractCryptoValuesFromCsv(eq("LTC_values.csv"))).thenReturn(getCryptoDocuments("LTC"));
        when(fileReader.extractCryptoValuesFromCsv(eq("XRP_values.csv"))).thenReturn(getCryptoDocuments("XRP"));

        LocalDate dateFrom = LocalDate.of(2022, 1, 1);
        LocalDate dateTo = LocalDate.of(2022, 1, 2);

        StepVerifier.create(repository.findAllByDateRange(dateFrom, dateTo))
                .assertNext(next -> {
                    assertEquals(new Timestamp(1641009600000L), next.timestamp());
                    assertEquals("BTC", next.symbol());
                    assertEquals(Double.valueOf(46813.21), Double.valueOf(next.price()));
                })
                .assertNext(next -> {
                    assertEquals(new Timestamp(1641020400000L), next.timestamp());
                    assertEquals("BTC", next.symbol());
                    assertEquals(Double.valueOf(46814.18), Double.valueOf(next.price()));
                })
                .assertNext(next -> {
                    assertEquals(new Timestamp(1641009600000L), next.timestamp());
                    assertEquals("DOGE", next.symbol());
                    assertEquals(Double.valueOf(0.1702), Double.valueOf(next.price()));
                })
                .assertNext(next -> {
                    assertEquals(new Timestamp(1641074400000L), next.timestamp());
                    assertEquals("DOGE", next.symbol());
                    assertEquals(Double.valueOf(0.1701), Double.valueOf(next.price()));
                })
                .assertNext(next -> {
                    assertEquals(new Timestamp(1641024000000L), next.timestamp());
                    assertEquals("ETH", next.symbol());
                    assertEquals(Double.valueOf(3715.32), Double.valueOf(next.price()));
                })
                .assertNext(next -> {
                    assertEquals(new Timestamp(1641031200000L), next.timestamp());
                    assertEquals("ETH", next.symbol());
                    assertEquals(Double.valueOf(3718.67), Double.valueOf(next.price()));
                })
                .assertNext(next -> {
                    assertEquals(new Timestamp(1641016800000L), next.timestamp());
                    assertEquals("LTC", next.symbol());
                    assertEquals(Double.valueOf(148.1), Double.valueOf(next.price()));
                })
                .assertNext(next -> {
                    assertEquals(new Timestamp(1641063600000L), next.timestamp());
                    assertEquals("LTC", next.symbol());
                    assertEquals(Double.valueOf(150.2), Double.valueOf(next.price()));
                })
                .assertNext(next -> {
                    assertEquals(new Timestamp(1640995200000L), next.timestamp());
                    assertEquals("XRP", next.symbol());
                    assertEquals(Double.valueOf(0.8298), Double.valueOf(next.price()));
                })
                .assertNext(next -> {
                    assertEquals(new Timestamp(1641016800000L), next.timestamp());
                    assertEquals("XRP", next.symbol());
                    assertEquals(Double.valueOf(0.842), Double.valueOf(next.price()));
                })
                .verifyComplete();

        verify(fileReader, times(5)).extractCryptoValuesFromCsv(anyString());
    }

    // -----------------------------------
    // Tests: findBySymbolAndDateRange
    // -----------------------------------
    @Test
    public void test_findBySymbolAndDateRange() {

        when(fileReader.extractCryptoValuesFromCsv(eq("BTC_values.csv"))).thenReturn(getCryptoDocuments("BTC"));

        LocalDate dateFrom = LocalDate.of(2022, 1, 1);
        LocalDate dateTo = LocalDate.of(2022, 1, 2);

        StepVerifier.create(repository.findBySymbolAndDateRange("BTC", dateFrom, dateTo))
                .assertNext(next -> {
                    assertEquals(new Timestamp(1641009600000L), next.timestamp());
                    assertEquals("BTC", next.symbol());
                    assertEquals(Double.valueOf(46813.21), Double.valueOf(next.price()));
                })
                .assertNext(next -> {
                    assertEquals(new Timestamp(1641020400000L), next.timestamp());
                    assertEquals("BTC", next.symbol());
                    assertEquals(Double.valueOf(46814.18), Double.valueOf(next.price()));
                })
                .verifyComplete();

        verify(fileReader).extractCryptoValuesFromCsv("BTC_values.csv");
    }

    // -----------------------------------
    // Tests: findAllByDate
    // -----------------------------------
    @Test
    public void test_findAllByDate() {

        when(fileReader.extractCryptoValuesFromCsv(eq("BTC_values.csv"))).thenReturn(getCryptoDocuments("BTC"));
        when(fileReader.extractCryptoValuesFromCsv(eq("DOGE_values.csv"))).thenReturn(getCryptoDocuments("DOGE"));
        when(fileReader.extractCryptoValuesFromCsv(eq("ETH_values.csv"))).thenReturn(getCryptoDocuments("ETH"));
        when(fileReader.extractCryptoValuesFromCsv(eq("LTC_values.csv"))).thenReturn(getCryptoDocuments("LTC"));
        when(fileReader.extractCryptoValuesFromCsv(eq("XRP_values.csv"))).thenReturn(getCryptoDocuments("XRP"));

        LocalDate day = LocalDate.of(2022, 1, 22);

        StepVerifier.create(repository.findAllByDate(day))
                .assertNext(next -> {
                    assertEquals("BTC", next.symbol());
                    assertEquals(new Timestamp(1642838400000L), next.timestamp());
                    assertEquals(Double.valueOf(47001.28), Double.valueOf(next.price()));
                })
                .assertNext(next -> {
                    assertEquals("DOGE", next.symbol());
                    assertEquals(new Timestamp(1642838900000L), next.timestamp());
                    assertEquals(Double.valueOf(0.1711), Double.valueOf(next.price()));
                })
                .assertNext(next -> {
                    assertEquals("ETH", next.symbol());
                    assertEquals(new Timestamp(1642838800000L), next.timestamp());
                    assertEquals(Double.valueOf(3697.04), Double.valueOf(next.price()));
                })
                .assertNext(next -> {
                    assertEquals("LTC", next.symbol());
                    assertEquals(new Timestamp(1642838700000L), next.timestamp());
                    assertEquals(Double.valueOf(150), Double.valueOf(next.price()));
                })
                .assertNext(next -> {
                    assertEquals("XRP", next.symbol());
                    assertEquals(new Timestamp(1642838600000L), next.timestamp());
                    assertEquals(Double.valueOf(0.8458), Double.valueOf(next.price()));
                })
                .verifyComplete();

        verify(fileReader, times(5)).extractCryptoValuesFromCsv(anyString());
    }


    // ---------------------------------------------------------------------------------------------------------
    private List<CryptoPriceDocument> getCryptoDocuments(String crypto) {
        return switch (crypto) {
            case "BTC" -> Arrays.asList(
                    new CryptoPriceDocument(new Timestamp(1641009600000L), "BTC", 46813.21),
                    new CryptoPriceDocument(new Timestamp(1641020400000L), "BTC", 46814.18),
                    new CryptoPriceDocument(new Timestamp(1642838400000L), "BTC", 47001.28));
            case "DOGE" -> Arrays.asList(
                    new CryptoPriceDocument(new Timestamp(1641009600000L), "DOGE", 0.1702),
                    new CryptoPriceDocument(new Timestamp(1641074400000L), "DOGE", 0.1701),
                    new CryptoPriceDocument(new Timestamp(1642838900000L), "DOGE", 0.1711));
            case "ETH" -> Arrays.asList(
                    new CryptoPriceDocument(new Timestamp(1641024000000L), "ETH", 3715.32),
                    new CryptoPriceDocument(new Timestamp(1641031200000L), "ETH", 3718.67),
                    new CryptoPriceDocument(new Timestamp(1642838800000L), "ETH", 3697.04));
            case "LTC" -> Arrays.asList(
                    new CryptoPriceDocument(new Timestamp(1641016800000L), "LTC", 148.1),
                    new CryptoPriceDocument(new Timestamp(1641063600000L), "LTC", 150.2),
                    new CryptoPriceDocument(new Timestamp(1642838700000L), "LTC", 150));
            case "XRP" -> Arrays.asList(
                    new CryptoPriceDocument(new Timestamp(1640995200000L), "XRP", 0.8298),
                    new CryptoPriceDocument(new Timestamp(1641016800000L), "XRP", 0.842),
                    new CryptoPriceDocument(new Timestamp(1642838600000L), "XRP", 0.8458));
            default -> Collections.emptyList();
        };
    }
}
