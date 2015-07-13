package pl.nkoder.katas.vendingmachine;

import pl.nkoder.katas.vendingmachine.display.Display;
import pl.nkoder.katas.vendingmachine.money.Coin;
import pl.nkoder.katas.vendingmachine.money.Coins;
import pl.nkoder.katas.vendingmachine.products.Product;
import pl.nkoder.katas.vendingmachine.shelves.Shelves;
import pl.nkoder.katas.vendingmachine.states.VendingMachineStateContext;
import pl.nkoder.katas.vendingmachine.states.WaitingForShelfChoiceState;
import pl.nkoder.katas.vendingmachine.time.DelayedActions;
import pl.nkoder.katas.vendingmachine.tray.Tray;

public class VendingMachine {

    private final Display display = new Display();
    private final Tray<Product> takeOutTray = new Tray<>();
    private final Tray<Coin> returnedCoinsTray = new Tray<>();
    private final VendingMachineStateContext stateContext;

    public VendingMachine(Shelves shelves, DelayedActions delayedActions) {
        Coins coins = new Coins();
        this.stateContext = new VendingMachineStateContext(shelves, display, coins, delayedActions, takeOutTray, returnedCoinsTray);
        stateContext.changeStateTo(new WaitingForShelfChoiceState(stateContext));
    }

    public String displayedMessage() {
        return display.message();
    }

    public void choose(int shelfNumber) {
        stateContext.currentState().handleChoiceOfShelfNumber(shelfNumber);
        updateDisplay();
    }

    public void insert(Coin coin) {
        stateContext.currentState().handleCoinInsertion(coin);
        updateDisplay();
    }

    public void cancel() {
        stateContext.currentState().handleCancellation();
        updateDisplay();
    }

    public Iterable<Coin> returnedCoins() {
        return returnedCoinsTray.all();
    }

    public Iterable<Product> productsInTakeOutTray() {
        return takeOutTray.all();
    }

    public Iterable<Product> takeAllProductsFromTakeOutTray() {
        Iterable<Product> products = takeOutTray.all();
        takeOutTray.removeAll();
        return products;
    }

    private void updateDisplay() {
        stateContext.currentState().handleUpdateOf(display);
    }

}
