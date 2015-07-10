package pl.nkoder.katas.vendingmachine;

import org.junit.Test;
import pl.nkoder.katas.vendingmachine.money.Coin;
import pl.nkoder.katas.vendingmachine.shelves.Shelves;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.nkoder.katas.vendingmachine.money.Coin.COIN_0_5;
import static pl.nkoder.katas.vendingmachine.money.Coin.COIN_1_0;
import static pl.nkoder.katas.vendingmachine.money.Coin.COIN_2_0;
import static pl.nkoder.katas.vendingmachine.money.Cost.costOf;
import static pl.nkoder.katas.vendingmachine.products.ProductsForTests.COLA;
import static pl.nkoder.katas.vendingmachine.products.ProductsForTests.MARS;

public class VendingMachineShould {

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

        Shelves shelves = new Shelves().putProduct(COLA, FIRST_SHELF, costOf("3.5"));
        VendingMachine machine = new VendingMachine(shelves);

        machine.choose(FIRST_SHELF);

        assertThat(machine.displayedMessage()).isEqualTo("Insert 3.5");
    }

    @Test
    public void
    ask_for_product_after_cancellation_of_previous_choice() {

        Shelves shelves = new Shelves().putProduct(COLA, FIRST_SHELF, costOf("3.5"));
        VendingMachine machine = new VendingMachine(shelves);
        machine.choose(FIRST_SHELF);

        machine.cancel();

        assertThat(machine.displayedMessage()).isEqualTo("Choose a product");
    }

    @Test
    public void
    show_price_of_new_chosen_product_after_cancellation_of_previous_choice() {

        Shelves shelves = new Shelves()
            .putProduct(COLA, FIRST_SHELF, costOf("3.5"))
            .putProduct(MARS, SECOND_SHELF, costOf("1.0"));
        VendingMachine machine = new VendingMachine(shelves);
        machine.choose(FIRST_SHELF);
        machine.cancel();

        machine.choose(SECOND_SHELF);

        assertThat(machine.displayedMessage()).isEqualTo("Insert 1.0");
    }

    @Test
    public void
    show_remaining_cost_of_chosen_product() {

        Shelves shelves = new Shelves().putProduct(COLA, FIRST_SHELF, costOf("3.5"));
        VendingMachine machine = new VendingMachine(shelves);
        machine.choose(FIRST_SHELF);

        machine.insert(COIN_2_0);
        machine.insert(COIN_0_5);

        assertThat(machine.displayedMessage()).isEqualTo("Insert 1.0");
    }

    @Test
    public void
    return_inserted_coins_on_cancellation() {

        Shelves shelves = new Shelves().putProduct(COLA, FIRST_SHELF, costOf("3.5"));
        VendingMachine machine = new VendingMachine(shelves);
        machine.choose(FIRST_SHELF);
        machine.insert(COIN_0_5);
        machine.insert(COIN_0_5);
        machine.insert(COIN_1_0);
        machine.insert(COIN_0_5);

        machine.cancel();

        assertThat(asMap(machine.returnedCoins()))
            .hasSize(2)
            .containsEntry(COIN_0_5, 3L)
            .containsEntry(COIN_1_0, 1L);
    }

    private Map<Coin, Long> asMap(List<Coin> coins) {
        return coins
            .stream()
            .collect(groupingBy(coin -> coin, counting()));
    }

}
