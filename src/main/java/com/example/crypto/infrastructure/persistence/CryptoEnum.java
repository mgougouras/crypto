package com.example.crypto.infrastructure.persistence;

import lombok.Getter;

public enum CryptoEnum {

    BTC("BTC_values.csv"),

    DOGE("DOGE_values.csv"),

    ETH("ETH_values.csv"),

    LTC("LTC_values.csv"),

    XRP("XRP_values.csv");

    @Getter
    private final String filePath;

    CryptoEnum(String filePath) {
        this.filePath = filePath;
    }
}
