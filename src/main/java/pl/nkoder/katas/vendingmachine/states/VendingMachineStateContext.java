package pl.nkoder.katas.vendingmachine.states;

import pl.nkoder.katas.vendingmachine.display.Display;
import pl.nkoder.katas.vendingmachine.money.Coin;
import pl.nkoder.katas.vendingmachine.money.Coins;
import pl.nkoder.katas.vendingmachine.money.Cost;
import pl.nkoder.katas.vendingmachine.products.Product;
import pl.nkoder.katas.vendingmachine.shelves.Shelves;
import pl.nkoder.katas.vendingmachine.time.DelayedAction;
import pl.nkoder.katas.vendingmachine.time.DelayedActions;
import pl.nkoder.katas.vendingmachine.tray.Tray;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class VendingMachineStateContext {

    private final Shelves shelves;
    private final Display display;
    private final Coins allCoins;

    private final DelayedActions delayedActions;
    private final Tray<Product> takeOutTray;
    private final Tray<Coin> returnedCoinsTray;
    private VendingMachineState state;

    public VendingMachineStateContext(Shelves shelves, Display display, Coins allCoins, DelayedActions delayedActions, Tray<Product> takeOutTray, Tray<Coin> returnedCoinsTray) {
        this.shelves = shelves;
        this.display = display;
        this.allCoins = allCoins;
        this.delayedActions = delayedActions;
        this.takeOutTray = takeOutTray;
        this.returnedCoinsTray = returnedCoinsTray;
        changeStateTo(new WaitingForShelfChoiceState(this));
    }

    public VendingMachineState currentState() {
        return state;
    }

    public void changeStateTo(VendingMachineState nextState) {
        state = nextState;
        state.handleUpdateOf(display);
    }

    public Cost priceOfProductAtShelf(int shelfNumber) {
        return shelves.priceOfProductAtShelf(shelfNumber);
    }

    public Product productAtShelf(int shelfNumber) {
        return shelves.productAtShelf(shelfNumber);
    }

    public void addCoin(Coin coin) {
        allCoins.add(coin);
    }

    public Optional<List<Coin>> takeCoinsOfValueOf(Cost cost) {
        return allCoins.takeEquivalentOf(cost);
    }

    public void sellProductAtShelf(int shelfNumber) {
        takeOutTray.put(productAtShelf(shelfNumber));
    }

    public void returnCoins(List<Coin> coins) {
        coins.forEach(takenCoin -> returnedCoinsTray.put(takenCoin));
    }

    public DelayedAction after(Duration duration) {
        return delayedActions.after(duration);
    }
}
