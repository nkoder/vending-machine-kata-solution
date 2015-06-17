package pl.nkoder.katas.vendingmachine;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Price that = (Price) other;
        return new EqualsBuilder()
            .append(this.value, that.value)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(value)
            .toHashCode();
    }
}
