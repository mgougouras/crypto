package com.example.crypto.application.getboundvalues;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A view holding data about bound values of a crypto -- specifically : oldest, newest, min and max values.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetBoundValuesView {

    /**
     * The crypto's oldest value.
     */
    private Double oldestValue;

    /**
     * The crypto's newest value.
     */
    private Double newestValue;

    /**
     * The crypto's min value.
     */
    private Double minValue;

    /**
     * The crypto's max value.
     */
    private Double maxValue;
}
