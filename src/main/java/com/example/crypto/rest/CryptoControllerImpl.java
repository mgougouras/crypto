package com.example.crypto.rest;

import com.example.crypto.application.CryptoApplicationService;
import com.example.crypto.application.getboundvalues.GetBoundValuesView;
import com.example.crypto.application.gethighestnormalized.GetHighestNormalizedView;
import com.example.crypto.application.getnormalizedrange.GetNormalizedRangeView;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cryptos")
public class CryptoControllerImpl implements CryptoController {

    private final CryptoApplicationService cryptoApplicationService;

    /**
     * Retrieves a sorted list of all the cryptos, comparing the normalized range (i.e. (max-min)/min)"
     *
     * @param dateFrom   The date from which the client is interested in.
     * @param dateTo     The date to which the client is interested in.
     * @return A List of GetNormalizedRangeView
     */
    @GetMapping(path = "/normalizedRange")
    public Mono<List<GetNormalizedRangeView>> getNormalizedRange(
            @RequestParam(value = "dateFrom", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(value = "dateTo", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {

        return cryptoApplicationService.getNormalizedRange(dateFrom, dateTo);
    }

    @GetMapping(path = "/{symbol}/boundValues")
    public Mono<GetBoundValuesView> getBoundValues(
            @PathVariable(value = "symbol") String symbol,
            @RequestParam(value = "dateFrom", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(value = "dateTo", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {

        return cryptoApplicationService.getBoundValues(symbol, dateFrom, dateTo);
    }

    @GetMapping(path = "/normalizedRange/highest")
    public Mono<GetHighestNormalizedView> getHighestNormalized(
            @RequestParam(value = "day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {

        return cryptoApplicationService.getHighestNormalized(day);
    }
}
