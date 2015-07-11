package pl.nkoder.katas.vendingmachine;

import org.junit.Before;
import org.junit.Test;
import pl.nkoder.katas.vendingmachine.shelves.Shelves;
import pl.nkoder.katas.vendingmachine.time.DelayedActionsForTests;

import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
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
    private Shelves shelves;
    private DelayedActionsForTests delayedActions;

    @Before
    public void setUp() {
        shelves = new Shelves();
        delayedActions = new DelayedActionsForTests();
    }

    @Test
    public void
    by_default_ask_for_product_choice() {

        VendingMachine machine = new VendingMachine(shelves, delayedActions);

        assertThat(machine.displayedMessage()).isEqualTo("Wybierz produkt");
        assertThat(machine.takeOutTray()).isEmpty();
        assertThat(machine.returnedCoins()).isEmpty();
    }

    @Test
    public void
    show_price_of_chosen_product() {

        shelves.putProduct(COLA, FIRST_SHELF, costOf("3.5"));
        VendingMachine machine = new VendingMachine(shelves, delayedActions);

        machine.choose(FIRST_SHELF);

        assertThat(machine.displayedMessage()).isEqualTo("Wrzuć 3.5");
        assertThat(machine.takeOutTray()).isEmpty();
        assertThat(machine.returnedCoins()).isEmpty();
    }

    @Test
    public void
    ask_for_product_after_cancellation_of_previous_choice() {

        shelves.putProduct(COLA, FIRST_SHELF, costOf("3.5"));
        VendingMachine machine = new VendingMachine(shelves, delayedActions);

        machine.choose(FIRST_SHELF);

        machine.cancel();

        assertThat(machine.displayedMessage()).isEqualTo("Wybierz produkt");
        assertThat(machine.takeOutTray()).isEmpty();
        assertThat(machine.returnedCoins()).isEmpty();
    }

    @Test
    public void
    show_price_of_new_chosen_product_after_cancellation_of_previous_choice() {

        shelves
            .putProduct(COLA, FIRST_SHELF, costOf("3.5"))
            .putProduct(MARS, SECOND_SHELF, costOf("1.0"));
        VendingMachine machine = new VendingMachine(shelves, delayedActions);

        machine.choose(FIRST_SHELF);
        machine.cancel();

        machine.choose(SECOND_SHELF);

        assertThat(machine.displayedMessage()).isEqualTo("Wrzuć 1.0");
        assertThat(machine.takeOutTray()).isEmpty();
        assertThat(machine.returnedCoins()).isEmpty();
    }

    @Test
    public void
    show_remaining_cost_of_chosen_product() {

        shelves.putProduct(COLA, FIRST_SHELF, costOf("3.5"));
        VendingMachine machine = new VendingMachine(shelves, delayedActions);

        machine.choose(FIRST_SHELF);

        machine.insert(COIN_2_0);
        machine.insert(COIN_0_5);

        assertThat(machine.displayedMessage()).isEqualTo("Wrzuć 1.0");
        assertThat(machine.takeOutTray()).isEmpty();
        assertThat(machine.returnedCoins()).isEmpty();
    }

    @Test
    public void
    return_inserted_coins_on_cancellation() {

        shelves.putProduct(COLA, FIRST_SHELF, costOf("3.5"));
        VendingMachine machine = new VendingMachine(shelves, delayedActions);

        machine.choose(FIRST_SHELF);

        machine.insert(COIN_0_5);
        machine.insert(COIN_0_5);
        machine.insert(COIN_1_0);
        machine.insert(COIN_0_5);

        machine.cancel();

        assertThat(machine.displayedMessage()).isEqualTo("Wybierz produkt");
        assertThat(machine.takeOutTray()).isEmpty();
        assertThat(asMap(machine.returnedCoins()))
            .hasSize(2)
            .containsEntry(COIN_0_5, 3L)
            .containsEntry(COIN_1_0, 1L);
    }

    @Test
    public void
    sell_chosen_product_after_inserting_enough_coins() {

        shelves.putProduct(COLA, FIRST_SHELF, costOf("3.5"));
        VendingMachine machine = new VendingMachine(shelves, delayedActions);

        machine.choose(FIRST_SHELF);

        machine.insert(COIN_2_0);
        machine.insert(COIN_1_0);
        machine.insert(COIN_0_5);

        assertThat(machine.displayedMessage()).isEqualTo("Wybierz produkt");
        assertThat(asMap(machine.takeOutTray())).hasSize(1).containsEntry(COLA, 1L);
        assertThat(machine.returnedCoins()).isEmpty();
    }

    @Test
    public void
    return_change_after_selling_product_if_inserted_more_coins_than_needed() {

        shelves.putProduct(COLA, FIRST_SHELF, costOf("3.5"));
        VendingMachine machine = new VendingMachine(shelves, delayedActions);

        machine.choose(FIRST_SHELF);

        machine.insert(COIN_2_0);
        machine.insert(COIN_0_5);
        machine.insert(COIN_0_5);
        machine.insert(COIN_1_0);

        assertThat(machine.displayedMessage()).isEqualTo("Wybierz produkt");
        assertThat(asMap(machine.takeOutTray())).hasSize(1).containsEntry(COLA, 1L);
        assertThat(asMap(machine.returnedCoins())).hasSize(1).containsEntry(COIN_0_5, 1L);
    }

    @Test
    public void
    return_all_coins_after_try_of_selling_product_if_cannot_give_the_change() {

        shelves.putProduct(COLA, FIRST_SHELF, costOf("3.5"));
        VendingMachine machine = new VendingMachine(shelves, delayedActions);

        machine.choose(FIRST_SHELF);

        machine.insert(COIN_2_0);
        machine.insert(COIN_1_0);
        machine.insert(COIN_1_0);

        assertThat(machine.displayedMessage()).isEqualTo("Nie mogę wydać reszty. Zakup anulowany.");
        assertThat(machine.takeOutTray()).isEmpty();
        assertThat(asMap(machine.returnedCoins()))
            .hasSize(2)
            .containsEntry(COIN_2_0, 1L)
            .containsEntry(COIN_1_0, 2L);

        delayedActions.performAll();

        assertThat(machine.displayedMessage()).isEqualTo("Wybierz produkt");
    }

    private <T> Map<T, Long> asMap(Iterable<T> coins) {
        return newArrayList(coins)
            .stream()
            .collect(groupingBy(coin -> coin, counting()));
    }

}
