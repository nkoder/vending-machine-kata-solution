package pl.nkoder.katas.vendingmachine;

import pl.nkoder.katas.vendingmachine.display.Display;
import pl.nkoder.katas.vendingmachine.money.Coin;
import pl.nkoder.katas.vendingmachine.money.Coins;
import pl.nkoder.katas.vendingmachine.products.Product;
import pl.nkoder.katas.vendingmachine.shelves.Shelves;
import pl.nkoder.katas.vendingmachine.states.VendingMachineState;
import pl.nkoder.katas.vendingmachine.states.VendingMachineStateContext;
import pl.nkoder.katas.vendingmachine.time.DelayedActions;
import pl.nkoder.katas.vendingmachine.tray.Tray;

public class VendingMachine {

    private final Display display = new Display();
    private final Tray<Product> takeOutTray = new Tray<>();
    private final Tray<Coin> returnedCoinsTray = new Tray<>();
    private final VendingMachineStateContext stateContext;

    public VendingMachine(Shelves shelves, DelayedActions delayedActions) {
        Coins coins = new Coins();
        stateContext = new VendingMachineStateContext(
            shelves, display, coins, delayedActions, takeOutTray, returnedCoinsTray); // TODO group sth?
    }

    public String displayedMessage() {
        return display.message();
    }

    public void choose(int shelfNumber) {
        currentState().handleChoiceOfShelfNumber(shelfNumber);
    }

    public void insert(Coin coin) {
        currentState().handleCoinInsertion(coin);
    }

    public void cancel() {
        currentState().handleCancellation();
    }

    public Iterable<Coin> returnedCoins() {
        return returnedCoinsTray.listAll();
    }

    public Iterable<Product> productsInTakeOutTray() {
        return takeOutTray.listAll();
    }

    public Iterable<Product> takeAllProductsFromTakeOutTray() {
        return takeOutTray.takeAll();
    }

    private VendingMachineState currentState() {
        return stateContext.currentState();
    }

}
