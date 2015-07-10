package pl.nkoder.katas.vendingmachine;

public class Price {

    private final String value;

    public static Price price(String value) {
        return new Price(value);
    }

    private Price(String value) {
        this.value = value;
    }

    public String asText() {
        return value;
    }
}
