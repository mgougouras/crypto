package com.example.crypto.infrastructure.persistence;

import java.time.LocalDate;
import reactor.core.publisher.Flux;

public interface CryptoDocumentRepository {

    /**
     * Retrieves all {@link CryptoPriceDocument}s matching the given dates.
     *
     * @param dateFrom   The starting date to search for.
     * @param dateTo     The end date to search for.
     * @return A FLux of {@link CryptoPriceDocument}
     */
    Flux<CryptoPriceDocument> findAllByDateRange(LocalDate dateFrom, LocalDate dateTo);

    /**
     * Retrieves all {@link CryptoPriceDocument}s matching the given symbol.
     *
     * @param symbol     The crypto symbol to search for.
     * @param dateFrom   The starting date to search for.
     * @param dateTo     The end date to search for.
     * @return A FLux of {@link CryptoPriceDocument}
     */
    Flux<CryptoPriceDocument> findBySymbolAndDateRange(String symbol, LocalDate dateFrom, LocalDate dateTo);

    /**
     * Retrieves all {@link CryptoPriceDocument}s for the given day.
     *
     * @param day        The specific date to search for.
     * @return A FLux of {@link CryptoPriceDocument}
     */
    Flux<CryptoPriceDocument> findAllByDate(LocalDate day);
}
