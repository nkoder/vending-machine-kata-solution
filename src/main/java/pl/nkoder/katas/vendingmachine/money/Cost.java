package pl.nkoder.katas.vendingmachine.money;

import java.math.BigDecimal;

public class Cost {

    private final BigDecimal value;

    public static Cost costOf(String valueAsText) {
        return new Cost(new BigDecimal(valueAsText));
    }

    private Cost(BigDecimal value) {
        this.value = value;
    }

    public Cost add(Cost anotherCost) {
        return new Cost(value.add(anotherCost.value));
    }

    public Cost subtract(Cost anotherCost) {
        return new Cost(value.subtract(anotherCost.value));
    }

    public String asText() {
        return value.toString();
    }
}
