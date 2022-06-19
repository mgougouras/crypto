package com.example.crypto.application;

import static java.util.stream.Collectors.groupingBy;

import com.example.crypto.application.getboundvalues.GetBoundValuesView;
import com.example.crypto.application.gethighestnormalized.GetHighestNormalizedView;
import com.example.crypto.application.getnormalizedrange.GetNormalizedRangeView;
import com.example.crypto.infrastructure.exception.PricesNotFoundException;
import com.example.crypto.infrastructure.exception.ValidationException;
import com.example.crypto.infrastructure.persistence.CryptoDocumentRepository;
import com.example.crypto.infrastructure.persistence.CryptoEnum;
import com.example.crypto.infrastructure.persistence.CryptoPriceDocument;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CryptoApplicationServiceImpl implements CryptoApplicationService {

    private final CryptoDocumentRepository repository;

    @Override
    public Mono<List<GetNormalizedRangeView>> getNormalizedRange(LocalDate dateFrom, LocalDate dateTo) {

        if (!validDateRange(dateFrom, dateTo)) {
            return Mono.error(new ValidationException("DateFrom must be less or equal to dateTo"));
        }

        return repository.findAllByDateRange(dateFrom, dateTo)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new PricesNotFoundException())))
                .collectList()
                .map(list -> {

                    // we create a map of crypto prices grouped by symbol
                    Map<String, List<CryptoPriceDocument>> pricesPerSymbol = list.stream()
                            .collect(groupingBy(CryptoPriceDocument::symbol));

                    return pricesPerSymbol.entrySet().stream()
                            .map(entry -> {
                                List<Double> prices = entry.getValue().stream()
                                        .map(CryptoPriceDocument::price)
                                        .toList();

                                double normalizedRange = getNormalizedRange(prices);
                                String symbol = entry.getKey();

                                return new GetNormalizedRangeView(symbol, normalizedRange);
                            })
                            .sorted(Comparator.comparing(GetNormalizedRangeView::getRange).reversed())
                            .collect(Collectors.toList());
                });
    }

    @Override
    public Mono<GetBoundValuesView> getBoundValues(String symbol, LocalDate dateFrom, LocalDate dateTo) {

        if (!validCryptoSymbol(symbol)) {
            return Mono.error(new ValidationException("Crypto is not valid or not supported"));
        }
        if (!validDateRange(dateFrom, dateTo)) {
            return Mono.error(new ValidationException("DateFrom must be less or equal to dateTo"));
        }

        return repository.findBySymbolAndDateRange(symbol, dateFrom, dateTo)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new PricesNotFoundException())))
                .collectList()
                .map(list -> {

                    double oldestPrice = list.stream()
                            .min(Comparator.comparing(CryptoPriceDocument::timestamp))
                            .map(CryptoPriceDocument::price)
                            .orElse((double) 0);

                    double newestPrice = list.stream()
                            .max(Comparator.comparing(CryptoPriceDocument::timestamp))
                            .map(CryptoPriceDocument::price)
                            .orElse((double) 0);

                    List<Double> prices = list.stream()
                            .map(CryptoPriceDocument::price)
                            .toList();

                    double maxPrice = Collections.max(prices, null);
                    double minPrice = Collections.min(prices, null);

                    return new GetBoundValuesView(oldestPrice, newestPrice, minPrice, maxPrice);
                });
    }

    @Override
    public Mono<GetHighestNormalizedView> getHighestNormalized(LocalDate day) {
        return repository.findAllByDate(day)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new PricesNotFoundException())))
                .collectList()
                .map(list -> {
                    // we create a map of crypto prices grouped by symbol
                    Map<String, List<CryptoPriceDocument>> pricesPerSymbol = list.stream()
                            .collect(groupingBy(CryptoPriceDocument::symbol));

                    Map<String, Double> rangePerCrypto = new HashMap<>();

                    // we calculate each crypto's normalized range and store into new map
                    pricesPerSymbol.forEach((symbol, values) -> {
                        List<Double> prices = values.stream()
                                .map(CryptoPriceDocument::price)
                                .toList();

                        double normalizedRange = getNormalizedRange(prices);

                        rangePerCrypto.put(symbol, normalizedRange);
                    });

                    // we extract the crypto with the highest normalized range
                    String symbol = Collections.max(rangePerCrypto.entrySet(), Map.Entry.comparingByValue()).getKey();
                    return new GetHighestNormalizedView(symbol, rangePerCrypto.get(symbol));
                });

    }

    /**
     * Calculates the normalized range from a list of prices.
     *
     * @param prices The list of prices.
     * @return The normalized range
     */
    private double getNormalizedRange(List<Double> prices) {
        double maxPrice = Collections.max(prices, null);
        double minPrice = Collections.min(prices, null);

        return (maxPrice - minPrice) / minPrice;
    }

    /**
     * Checks if the given dates are valid.
     *
     * @param dateFrom The date from.
     * @param dateTo   The date to.
     * @return Whether the date values are valid
     */
    private boolean validDateRange(LocalDate dateFrom, LocalDate dateTo) {
        return dateFrom == null || dateTo == null || dateFrom.compareTo(dateTo) <= 0;
    }

    /**
     * Checks if the given crypto symbol exist in our approved crypto enumeration.
     *
     * @param symbol The crypto symbol.
     * @return Whether the symbol is valid.
     */
    private boolean validCryptoSymbol(String symbol) {
        return !StringUtils.isBlank(symbol) && EnumUtils.isValidEnum(CryptoEnum.class, symbol);
    }
}
