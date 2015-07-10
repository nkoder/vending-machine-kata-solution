package pl.nkoder.katas.vendingmachine;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.nkoder.katas.vendingmachine.Price.price;

public class VendingMachineShould {

    private static final Product COLA = new Product("Coca-Cola 0.25l");
    private static final Product MARS = new Product("Mars");
    private static final int FIRST_SHELF = 1;
    private static final int SECOND_SHELF = 2;

    @Test
    public void
    by_default_ask_for_product_choice() {

        VendingMachine machine = new VendingMachine(new Shelves());

        assertThat(machine.displayedMessage()).isEqualTo("Choose a product");
    }

    @Test
    public void
    show_price_of_chosen_product() {

        Shelves shelves = new Shelves()
            .putProduct(COLA, FIRST_SHELF, price("3.5"));
        VendingMachine machine = new VendingMachine(shelves);

        machine.choose(FIRST_SHELF);

        assertThat(machine.displayedMessage()).isEqualTo("Insert 3.5");
    }

    @Test
    public void
    ask_for_product_after_cancellation_of_previous_choice() {

        Shelves shelves = new Shelves()
            .putProduct(COLA, FIRST_SHELF, price("3.5"));
        VendingMachine machine = new VendingMachine(shelves);
        machine.choose(FIRST_SHELF);

        machine.cancel();

        assertThat(machine.displayedMessage()).isEqualTo("Choose a product");
    }

}
