package pl.nkoder.katas.vendingmachine;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.nkoder.katas.vendingmachine.Price.price;

public class VendingMachineShould {

    private static final Product COLA = new Product("Coca-Cola 0.25l");

    @Test
    public void by_default_ask_for_product_choice() {
        VendingMachine machine = new VendingMachine(new Prices());

        assertThat(machine.displayedMessage()).isEqualTo("Choose a product");
    }

    @Test
    public void show_price_of_chosen_product() {
        Prices prices = new Prices();
        prices.addPrice(COLA, price("3.5"));
        VendingMachine machine = new VendingMachine(prices);

        machine.choose(COLA);

        assertThat(machine.displayedMessage()).isEqualTo("Insert 3.5");
    }
}
