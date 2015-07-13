package pl.nkoder.katas.vendingmachine;

import org.junit.Before;
import org.junit.Test;
import pl.nkoder.katas.vendingmachine.shelves.Shelves;
import pl.nkoder.katas.vendingmachine.time.DelayedActionsForTests;

import static pl.nkoder.katas.vendingmachine.VendingMachineAssertions.assertThat;
import static pl.nkoder.katas.vendingmachine.money.Coin.*;
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

        assertThat(machine)
            .displaysMessage("Wybierz produkt")
            .hasNoProductInTakeOutTray()
            .returnedNoCoins();
    }

    @Test
    public void
    show_price_of_chosen_product() {

        shelves.putProduct(COLA, FIRST_SHELF, costOf("3.5"));
        VendingMachine machine = new VendingMachine(shelves, delayedActions);

        machine.choose(FIRST_SHELF);

        assertThat(machine)
            .displaysMessage("Wrzuć 3.5")
            .hasNoProductInTakeOutTray()
            .returnedNoCoins();
    }

    @Test
    public void
    ask_for_product_after_cancellation_of_previous_choice() {

        shelves.putProduct(COLA, FIRST_SHELF, costOf("1.0"));
        VendingMachine machine = new VendingMachine(shelves, delayedActions);

        machine.choose(FIRST_SHELF);

        machine.cancel();

        assertThat(machine)
            .displaysMessage("Wybierz produkt")
            .hasNoProductInTakeOutTray()
            .returnedNoCoins();
    }

    @Test
    public void
    show_price_of_new_chosen_product_after_cancellation_of_previous_choice() {

        shelves
            .putProduct(COLA, FIRST_SHELF, costOf("3.5"))
            .putProduct(MARS, SECOND_SHELF, costOf("2.0"));
        VendingMachine machine = new VendingMachine(shelves, delayedActions);

        machine.choose(FIRST_SHELF);
        machine.cancel();

        machine.choose(SECOND_SHELF);

        assertThat(machine)
            .displaysMessage("Wrzuć 2.0")
            .hasNoProductInTakeOutTray()
            .returnedNoCoins();
    }

    @Test
    public void
    show_remaining_cost_of_chosen_product() {

        shelves.putProduct(COLA, FIRST_SHELF, costOf("3.5"));
        VendingMachine machine = new VendingMachine(shelves, delayedActions);

        machine.choose(FIRST_SHELF);

        machine.insert(COIN_2_0);
        machine.insert(COIN_0_5);

        assertThat(machine)
            .displaysMessage("Wrzuć 1.0")
            .hasNoProductInTakeOutTray()
            .returnedNoCoins();
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

        assertThat(machine)
            .displaysMessage("Wybierz produkt")
            .hasNoProductInTakeOutTray()
            .returnedCoins(COIN_0_5, COIN_0_5, COIN_0_5, COIN_1_0);
    }

    @Test
    public void
    sell_chosen_product_after_inserting_enough_coins() {

        shelves.putProduct(COLA, FIRST_SHELF, costOf("1.0"));
        VendingMachine machine = new VendingMachine(shelves, delayedActions);

        machine.choose(FIRST_SHELF);

        machine.insert(COIN_0_5);
        machine.insert(COIN_0_5);

        assertThat(machine)
            .displaysMessage("Wybierz produkt")
            .hasInTakeAwayTray(COLA)
            .returnedNoCoins();
    }

    @Test
    public void
    return_change_after_selling_product_if_inserted_more_coins_than_needed() {

        shelves.putProduct(COLA, FIRST_SHELF, costOf("1.0"));
        VendingMachine machine = new VendingMachine(shelves, delayedActions);

        machine.choose(FIRST_SHELF);

        machine.insert(COIN_0_5);
        machine.insert(COIN_1_0);

        assertThat(machine)
            .displaysMessage("Wybierz produkt")
            .hasInTakeAwayTray(COLA)
            .returnedCoins(COIN_0_5);
    }

    @Test
    public void
    return_all_coins_after_trial_of_selling_product_if_cannot_give_the_change() {

        shelves.putProduct(COLA, FIRST_SHELF, costOf("1.5"));
        VendingMachine machine = new VendingMachine(shelves, delayedActions);

        machine.choose(FIRST_SHELF);

        machine.insert(COIN_1_0);
        machine.insert(COIN_1_0);

        assertThat(machine)
            .displaysMessage("Nie mogę wydać reszty. Zakup anulowany.")
            .hasNoProductInTakeOutTray()
            .returnedCoins(COIN_1_0, COIN_1_0);

        delayedActions.performAll();

        assertThat(machine).displaysMessage("Wybierz produkt");
    }

    @Test
    public void
    consider_coins_from_previous_transactions_when_trying_to_give_the_change() {

        shelves.putProduct(COLA, FIRST_SHELF, costOf("1.0"));
        VendingMachine machine = new VendingMachine(shelves, delayedActions);

        machine.choose(FIRST_SHELF);
        machine.insert(COIN_1_0);
        machine.takeAllProductsFromTakeOutTray();

        machine.choose(FIRST_SHELF);

        machine.insert(COIN_2_0);

        assertThat(machine)
            .displaysMessage("Wybierz produkt")
            .hasInTakeAwayTray(COLA)
            .returnedCoins(COIN_1_0);
    }

    @Test
    public void
    return_only_coins_for_current_transaction_on_cancellation() {

        shelves.putProduct(COLA, FIRST_SHELF, costOf("2.0"));
        VendingMachine machine = new VendingMachine(shelves, delayedActions);

        machine.choose(FIRST_SHELF);
        machine.insert(COIN_2_0);
        machine.takeAllProductsFromTakeOutTray();

        machine.choose(FIRST_SHELF);

        machine.insert(COIN_1_0);

        machine.cancel();

        assertThat(machine)
            .displaysMessage("Wybierz produkt")
            .hasNoProductInTakeOutTray()
            .returnedCoins(COIN_1_0);
    }
}
