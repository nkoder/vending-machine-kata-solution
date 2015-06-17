package pl.nkoder.katas.vendingmachine;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.nkoder.katas.vendingmachine.Price.price;

public class PricesShould {

    @Test
    public void know_prices_of_products() {
        Product cola = new Product("Coca-Cola 0.33l");
        Product mars = new Product("Mars");
        Prices prices = new Prices();
        prices.addPrice(cola, price("0.1"));
        prices.addPrice(mars, price("999.9"));

        assertThat(prices.priceOf(mars)).isEqualTo(price("999.9"));
        assertThat(prices.priceOf(cola)).isEqualTo(price("0.1"));
    }
}
