package pl.nkoder.katas.vendingmachine;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.nkoder.katas.vendingmachine.Price.price;

public class ShelvesShould {

    @Test
    public void know_prices_of_products() {
        Product cola = new Product("Coca-Cola 0.33l");
        Product mars = new Product("Mars");
        Shelves shelves = new Shelves();
        shelves.putProduct(cola, 1, price("0.1"));
        shelves.putProduct(mars, 2, price("999.9"));

        assertThat(shelves.priceOfProductAtShelf(2)).isEqualTo(price("999.9"));
        assertThat(shelves.priceOfProductAtShelf(1)).isEqualTo(price("0.1"));
    }
}
