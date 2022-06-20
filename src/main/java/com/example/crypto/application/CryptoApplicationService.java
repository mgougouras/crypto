package com.example.crypto.application;

import com.example.crypto.application.getboundvalues.GetBoundValuesView;
import com.example.crypto.application.gethighestnormalized.GetHighestNormalizedView;
import com.example.crypto.application.getnormalizedrange.GetNormalizedRangeView;
import java.time.LocalDate;
import java.util.List;
import reactor.core.publisher.Mono;

public interface CryptoApplicationService {

    /**
     * Retrieves a sorted list of all the cryptos, comparing the normalized range (i.e. (max-min)/min)
     *
     * @param dateFrom The date from which the client is interested in.
     * @param dateTo   The date to which the client is interested in.
     * @return A List of GetNormalizedRangeView
     */
    Mono<List<GetNormalizedRangeView>> getNormalizedRange(LocalDate dateFrom, LocalDate dateTo);

    /**
     * Retrieves the bound values of a crypto -- specifically the oldest, newest, min and max values.
     *
     * @param symbol   The crypto symbol which the client is interested in.
     * @param dateFrom The date from which the client is interested in.
     * @param dateTo   The date to which the client is interested in.
     * @return A GetBoundValuesView
     */
    Mono<GetBoundValuesView> getBoundValues(String symbol, LocalDate dateFrom, LocalDate dateTo);

    /**
     * Retrieves the crypto with the highest normalized range for a specific date.
     *
     * @param day The date which the client is interested in.
     * @return A GetHighestNormalizedView
     */
    Mono<GetHighestNormalizedView> getHighestNormalized(LocalDate day);
}
