package com.loftschool.loftcoin.utils;

public enum Fiat {
    USD("$"),
    EUR("€"),
    RUB("\u20BD");

    public String symbol;

    Fiat(String symbol) {
        this.symbol = symbol;
    }
}
