package pl.nkoder.katas.vendingmachine;

import pl.nkoder.katas.vendingmachine.display.Display;
import pl.nkoder.katas.vendingmachine.memory.InsertedValueMemory;
import pl.nkoder.katas.vendingmachine.memory.ShelfChoiceMemory;
import pl.nkoder.katas.vendingmachine.money.Coin;
import pl.nkoder.katas.vendingmachine.money.Coins;
import pl.nkoder.katas.vendingmachine.money.Cost;
import pl.nkoder.katas.vendingmachine.products.Product;
import pl.nkoder.katas.vendingmachine.shelves.Shelves;
import pl.nkoder.katas.vendingmachine.time.DelayedActions;
import pl.nkoder.katas.vendingmachine.tray.Tray;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class VendingMachine {

    private static final Duration DURATION_OF_WARNING_MESSAGE = Duration.ofSeconds(3);

    private final Shelves shelves;
    private final DelayedActions delayedActions;
    private final Display display = new Display();
    private final Tray<Product> takeOutTray = new Tray<>();
    private final Tray<Coin> returnedCoinsTray = new Tray<>();
    private final Coins coins = new Coins();
    private final InsertedValueMemory insertedValueMemory = new InsertedValueMemory();
    private final ShelfChoiceMemory shelfChoiceMemory = new ShelfChoiceMemory();

    private boolean lastTryWasSuccessful = true;

    public VendingMachine(Shelves shelves, DelayedActions delayedActions) {
        this.shelves = shelves;
        this.delayedActions = delayedActions;
        display.promptForProductChoice();
    }

    public String displayedMessage() {
        return display.message();
    }

    public void choose(int shelfNumber) {
        shelfChoiceMemory.setChosenShelfNumberTo(shelfNumber);
        updateDisplay();
    }

    public void insert(Coin coin) {
        insertedValueMemory.add(coin.value);
        coins.add(coin);
        if (areEnoughCoinsInsertedToBuyChosenProduct()) {
            Cost change = insertedValueMemory.value().subtract(priceOfProductAtShelf(shelfChoiceMemory.chosenShelf()).get());
            Optional<List<Coin>> changeCoins = coins.takeEquivalentOf(change);
            if (changeCoins.isPresent()) {
                takeOutTray.put(productAtShelf(shelfChoiceMemory.chosenShelf()).get());
                changeCoins.get()
                    .forEach(takenCoin -> returnedCoinsTray.put(takenCoin));
                lastTryWasSuccessful = true;
            } else {
                coins.takeEquivalentOf(insertedValueMemory.value()).get()
                    .forEach(takenCoin -> returnedCoinsTray.put(takenCoin));
                lastTryWasSuccessful = false;
            }
            insertedValueMemory.clear();
            shelfChoiceMemory.clear();
        }
        updateDisplay();
    }

    public void cancel() {
        shelfChoiceMemory.clear();
        coins.takeEquivalentOf(insertedValueMemory.value()).get()
            .forEach(coin -> returnedCoinsTray.put(coin));
        updateDisplay();
    }

    public Iterable<Coin> returnedCoins() {
        return returnedCoinsTray.all();
    }

    public Iterable<Product> takeOutTray() {
        return takeOutTray.all();
    }

    private void updateDisplay() {
        if (!isProductChosen()) {
            if (lastTryWasSuccessful) {
                display.promptForProductChoice();
            } else {
                display.warnAboutNoChange();
                delayedActions
                    .after(DURATION_OF_WARNING_MESSAGE)
                    .perform(() -> display.promptForProductChoice());
            }
        } else {
            Optional<Cost> optionalProductPrice = priceOfProductAtShelf(shelfChoiceMemory.chosenShelf());
            Cost remainingCost = optionalProductPrice.get().subtract(insertedValueMemory.value());
            display.promptForMoneyToInsert(remainingCost);
        }
    }

    private boolean isProductChosen() {
        return shelfChoiceMemory.chosenShelf().isPresent();
    }

    private boolean areEnoughCoinsInsertedToBuyChosenProduct() {
        return priceOfProductAtShelf(shelfChoiceMemory.chosenShelf())
            .filter(price -> insertedValueMemory.value().isGreaterOrEqualTo(price))
            .isPresent();
    }

    private Optional<Cost> priceOfProductAtShelf(Optional<Integer> chosenShelfNumber) {
        return chosenShelfNumber.map(shelfNumber -> shelves.priceOfProductAtShelf(shelfNumber));
    }

    private Optional<Product> productAtShelf(Optional<Integer> chosenShelfNumber) {
        return chosenShelfNumber.map(shelfNumber -> shelves.productAtShelf(shelfNumber));
    }

    public Iterable<Product> takeAllProductsFromTakeOutTray() {
        Iterable<Product> products = takeOutTray.all();
        takeOutTray.removeAll();
        return products;
    }
}
