package com.example.crypto.application.getnormalizedrange;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A view holding data about the normalized range of a crypto.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetNormalizedRangeView {

    /**
     * The crypto's name.
     */
    private String name;

    /**
     * The crypto's normalized range.
     */
    private Double range;
}
