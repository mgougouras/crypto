package com.example.crypto.application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.crypto.infrastructure.exception.PricesNotFoundException;
import com.example.crypto.infrastructure.exception.ValidationException;
import com.example.crypto.infrastructure.persistence.CryptoDocumentRepository;
import com.example.crypto.infrastructure.persistence.CryptoPriceDocument;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(MockitoJUnitRunner.class)
public class CryptoApplicationServiceTest {

    @Mock
    CryptoDocumentRepository repository;

    @InjectMocks
    CryptoApplicationServiceImpl cryptoApplicationService;

    @Before
    public void setUp() {
        reset(repository);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(repository);
    }

    // -----------------------------------
    // Tests: getNormalizedRange
    // -----------------------------------
    @Test
    public void test_getNormalizedRange() {

        LocalDate dateFrom = LocalDate.of(2022, 1, 1);
        LocalDate dateTo = LocalDate.of(2022, 1, 3);

        when(repository.findAllByDateRange(eq(dateFrom), eq(dateTo)))
                .thenReturn(Flux.fromIterable(getCryptoDocuments()));

        StepVerifier.create(cryptoApplicationService.getNormalizedRange(dateFrom, dateTo))
                .assertNext(views -> {
                    assertEquals(5, views.size());

                    assertEquals("XRP", views.get(0).getName());
                    assertEquals(Double.valueOf(0.019281754639672227), views.get(0).getRange());

                    assertEquals("LTC", views.get(1).getName());
                    assertEquals(Double.valueOf(0.014179608372721097), views.get(1).getRange());

                    assertEquals("DOGE", views.get(2).getName());
                    assertEquals(Double.valueOf(0.005878894767783662), views.get(2).getRange());

                    assertEquals("ETH", views.get(3).getName());
                    assertEquals(Double.valueOf(0.005850626447103658), views.get(3).getRange());

                    assertEquals("BTC", views.get(4).getName());
                    assertEquals(Double.valueOf(0.004017455756612284), views.get(4).getRange());
                })
                .verifyComplete();

        verify(repository).findAllByDateRange(dateFrom, dateTo);
    }

    @Test
    public void test_getNormalizedRange_when_dateFromLargerThanDateTo() {

        LocalDate dateFrom = LocalDate.of(2022, 1, 5);
        LocalDate dateTo = LocalDate.of(2022, 1, 3);

        StepVerifier.create(cryptoApplicationService.getNormalizedRange(dateFrom, dateTo))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof ValidationException);
                    assertEquals("DateFrom must be less or equal to dateTo", error.getMessage());
                })
                .verify();
    }

    @Test
    public void test_getNormalizedRange_when_noPricesFound() {

        LocalDate dateFrom = LocalDate.of(2022, 1, 1);
        LocalDate dateTo = LocalDate.of(2022, 1, 3);

        when(repository.findAllByDateRange(eq(dateFrom), eq(dateTo))).thenReturn(Flux.empty());

        StepVerifier.create(cryptoApplicationService.getNormalizedRange(dateFrom, dateTo))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof PricesNotFoundException);
                    assertEquals("Prices not found for given criteria", error.getMessage());
                })
                .verify();

        verify(repository).findAllByDateRange(dateFrom, dateTo);
    }

    // -----------------------------------
    // Tests: getBoundValues
    // -----------------------------------
    @Test
    public void test_getBoundValues() {

        LocalDate dateFrom = LocalDate.of(2022, 1, 1);
        LocalDate dateTo = LocalDate.of(2022, 1, 3);

        List<CryptoPriceDocument> btcPriceDocuments = getCryptoDocuments().stream()
                .filter(doc -> doc.symbol().equals("BTC"))
                .toList();

        when(repository.findBySymbolAndDateRange(eq("BTC"), eq(dateFrom), eq(dateTo)))
                .thenReturn(Flux.fromIterable(btcPriceDocuments));

        StepVerifier.create(cryptoApplicationService.getBoundValues("BTC", dateFrom, dateTo))
                .assertNext(view -> {
                    assertEquals(Double.valueOf(46813.21), view.getMinValue());
                    assertEquals(Double.valueOf(47001.28), view.getMaxValue());
                    assertEquals(Double.valueOf(47001.28), view.getNewestValue());
                    assertEquals(Double.valueOf(46814.18), view.getOldestValue());
                })
                .verifyComplete();

        verify(repository).findBySymbolAndDateRange("BTC", dateFrom, dateTo);
    }

    @Test
    public void test_getBoundValues_when_dateFromLargerThanDateTo() {

        LocalDate dateFrom = LocalDate.of(2022, 1, 5);
        LocalDate dateTo = LocalDate.of(2022, 1, 3);

        StepVerifier.create(cryptoApplicationService.getBoundValues("BTC", dateFrom, dateTo))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof ValidationException);
                    assertEquals("DateFrom must be less or equal to dateTo", error.getMessage());
                })
                .verify();
    }

    @Test
    public void test_getBoundValues_when_noPricesFound() {

        LocalDate dateFrom = LocalDate.of(2022, 1, 1);
        LocalDate dateTo = LocalDate.of(2022, 1, 3);

        when(repository.findBySymbolAndDateRange(eq("BTC"), eq(dateFrom), eq(dateTo))).thenReturn(Flux.empty());

        StepVerifier.create(cryptoApplicationService.getBoundValues("BTC", dateFrom, dateTo))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof PricesNotFoundException);
                    assertEquals("Prices not found for given criteria", error.getMessage());
                })
                .verify();

        verify(repository).findBySymbolAndDateRange("BTC", dateFrom, dateTo);
    }

    @Test
    public void test_getBoundValues_when_symbolNotValid() {

        LocalDate dateFrom = LocalDate.of(2022, 1, 1);
        LocalDate dateTo = LocalDate.of(2022, 1, 3);


        StepVerifier.create(cryptoApplicationService.getBoundValues("ADA", dateFrom, dateTo))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof ValidationException);
                    assertEquals("Crypto is not valid or not supported", error.getMessage());
                })
                .verify();
    }

    // -----------------------------------
    // Tests: getHighestNormalized
    // -----------------------------------
    @Test
    public void test_getHighestNormalized() {

        LocalDate day = LocalDate.of(2022, 1, 1);

        List<CryptoPriceDocument> priceDocuments = getCryptoDocuments().stream()
                .filter(doc -> doc.timestamp().toLocalDateTime().toLocalDate().compareTo(day) == 0)
                .toList();

        when(repository.findAllByDate(eq(day))).thenReturn(Flux.fromIterable(priceDocuments));

        StepVerifier.create(cryptoApplicationService.getHighestNormalized(day))
                .assertNext(view -> {
                    assertEquals(Double.valueOf(0.019281754639672227), view.getRange());
                    assertEquals("XRP", view.getName());
                })
                .verifyComplete();

        verify(repository).findAllByDate(day);
    }

    @Test
    public void test_getHighestNormalized_when_noPricesFound() {

        LocalDate day = LocalDate.of(2022, 1, 1);

        when(repository.findAllByDate(eq(day))).thenReturn(Flux.empty());

        StepVerifier.create(cryptoApplicationService.getHighestNormalized(day))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof PricesNotFoundException);
                    assertEquals("Prices not found for given criteria", error.getMessage());
                })
                .verify();

        verify(repository).findAllByDate(day);
    }


    // ---------------------------------------------------------------------------------------------------------
    private List<CryptoPriceDocument> getCryptoDocuments() {
        return Arrays.asList(
                    new CryptoPriceDocument(new Timestamp(1641009600000L), "BTC", 46814.18),
                    new CryptoPriceDocument(new Timestamp(1641020400000L), "BTC", 46813.21),
                    new CryptoPriceDocument(new Timestamp(1641308400000L), "BTC", 47001.28),
                    new CryptoPriceDocument(new Timestamp(1641009600000L), "DOGE", 0.1702),
                    new CryptoPriceDocument(new Timestamp(1641074400000L), "DOGE", 0.1701),
                    new CryptoPriceDocument(new Timestamp(1641355200000L), "DOGE", 0.1711),
                    new CryptoPriceDocument(new Timestamp(1641024000000L), "ETH", 3715.32),
                    new CryptoPriceDocument(new Timestamp(1641031200000L), "ETH", 3718.67),
                    new CryptoPriceDocument(new Timestamp(1641049200000L), "ETH", 3697.04),
                    new CryptoPriceDocument(new Timestamp(1641016800000L), "LTC", 148.1),
                    new CryptoPriceDocument(new Timestamp(1641063600000L), "LTC", 150.2),
                    new CryptoPriceDocument(new Timestamp(1641078000000L), "LTC", 150),
                    new CryptoPriceDocument(new Timestamp(1640995200000L), "XRP", 0.8298),
                    new CryptoPriceDocument(new Timestamp(1641016800000L), "XRP", 0.842),
                    new CryptoPriceDocument(new Timestamp(1641070800000L), "XRP", 0.8458)
        );
    }
}
