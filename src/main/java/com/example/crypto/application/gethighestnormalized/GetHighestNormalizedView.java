package com.example.crypto.application.gethighestnormalized;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A view holding data about the crypto with the highest normalized range.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetHighestNormalizedView {

    /**
     * The crypto's name.
     */
    private String name;

    /**
     * The crypto's normalized range.
     */
    private Double range;
}
