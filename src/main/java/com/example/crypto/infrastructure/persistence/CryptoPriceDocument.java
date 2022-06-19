package com.example.crypto.infrastructure.persistence;

import java.sql.Timestamp;

/**
 * A record that represents the price of a crypto in specific timestamp.
 */
public record CryptoPriceDocument(Timestamp timestamp, String symbol, double price) {
}
