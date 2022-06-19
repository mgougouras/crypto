package com.example.crypto.rest;


import com.example.crypto.application.getboundvalues.GetBoundValuesView;
import com.example.crypto.application.gethighestnormalized.GetHighestNormalizedView;
import com.example.crypto.application.getnormalizedrange.GetNormalizedRangeView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

public interface CryptoController {

    @Operation(description = "Retrieves a sorted list of all the cryptos,\n" +
            "comparing the normalized range (i.e. (max-min)/min)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of GetNormalizedRangeView generated successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = GetNormalizedRangeView.class)
                                    )
                            )
                    })
    })
    Mono<List<GetNormalizedRangeView>> getNormalizedRange(
            @Parameter(description = "The date from which the client is interested in.")
            @RequestParam(value = "dateFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @Parameter(description = "The date to which the client is interested in.")
            @RequestParam(value = "dateTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo);

    // -------------------------------------------------------------------------------------------------------------

    @Operation(description = "Calculates the oldest, newest, min and max values of a given crypto")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "A GetBoundValuesView generated successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetBoundValuesView.class)
                            )
                    })
    })
    Mono<GetBoundValuesView> getBoundValues(
            @Parameter(description = "The crypto symbol that the client is interested in.")
            @PathVariable(value = "symbol") String symbol,
            @Parameter(description = "The date from which the client is interested in.")
            @RequestParam(value = "dateFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @Parameter(description = "The date to which the client is interested in.")
            @RequestParam(value = "dateTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo);

    // -------------------------------------------------------------------------------------------------------------

    @Operation(description = "Retrieves the crypto with the highest normalized range for a specific date")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "A GetHighestNormalizedView generated successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetHighestNormalizedView.class)
                            )
                    })
    })
    Mono<GetHighestNormalizedView> getHighestNormalized(
            @Parameter(description = "The date which the client is interested in.")
            @RequestParam(value = "day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day);
}
