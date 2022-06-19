package com.example.crypto.infrastructure.persistence;

import com.example.crypto.infrastructure.util.FileCryptoReader;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class CryptoDocumentRepositoryImpl implements CryptoDocumentRepository {

    private final FileCryptoReader fileCryptoReader;

    @Override
    public Flux<CryptoPriceDocument> findAllByDateRange(LocalDate dateFrom, LocalDate dateTo) {

        List<CryptoPriceDocument> allPrices = Stream.of(CryptoEnum.values())
                .map(crypto -> fileCryptoReader.extractCryptoValuesFromCsv(crypto.getFilePath()))
                .flatMap(List::stream)
                .filter(createDateFilters(dateFrom, dateTo).stream().reduce(x -> true, Predicate::and))
                .toList();

        return Flux.fromIterable(allPrices);
    }

    @Override
    public Flux<CryptoPriceDocument> findBySymbolAndDateRange(String symbol, LocalDate dateFrom, LocalDate dateTo) {

        return Flux.fromIterable(
                fileCryptoReader.extractCryptoValuesFromCsv(CryptoEnum.valueOf(symbol).getFilePath())
                        .stream()
                        .filter(createDateFilters(dateFrom, dateTo).stream().reduce(x -> true, Predicate::and))
                        .toList());
    }

    @Override
    public Flux<CryptoPriceDocument> findAllByDate(LocalDate day) {

        LocalDateTime localDateTime = day.atStartOfDay();
        Timestamp dayStart = Timestamp.valueOf(localDateTime.with(LocalTime.MIN));
        Timestamp dayEnd = Timestamp.valueOf(localDateTime.with(LocalTime.MAX));
        List<Predicate<CryptoPriceDocument>> allPredicates = new ArrayList<>();
        allPredicates.add(t -> t.timestamp().compareTo(dayStart) >= 0);
        allPredicates.add(t -> t.timestamp().compareTo(dayEnd) <= 0);

        List<CryptoPriceDocument> allPrices = Stream.of(CryptoEnum.values())
                .map(crypto -> fileCryptoReader.extractCryptoValuesFromCsv(crypto.getFilePath()))
                .flatMap(List::stream)
                .filter(allPredicates.stream().reduce(x -> true, Predicate::and))
                .toList();

        return Flux.fromIterable(allPrices);
    }

    /**
     * Creates date filters.
     *
     * @param dateFrom   The starting date to search for.
     * @param dateTo     The end date to search for.
     * @return A list of predicates.
     */
    private List<Predicate<CryptoPriceDocument>> createDateFilters(LocalDate dateFrom, LocalDate dateTo)  {

        List<Predicate<CryptoPriceDocument>> allPredicates = new ArrayList<>();
        if (dateFrom != null) {
            Timestamp from = Timestamp.valueOf(dateFrom.atStartOfDay());
            allPredicates.add(t -> t.timestamp().compareTo(from) >= 0);
        }
        if (dateTo != null) {
            LocalDateTime localDateTime = dateTo.atStartOfDay();
            Timestamp to = Timestamp.valueOf(localDateTime.with(LocalTime.MAX));
            allPredicates.add(t -> t.timestamp().compareTo(to) <= 0);
        }

        return allPredicates;
    }
}
