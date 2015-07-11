package pl.nkoder.katas.vendingmachine;

import pl.nkoder.katas.vendingmachine.display.Display;
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
    private final Coins coinsForChosenProduct = new Coins();

    private Optional<Integer> chosenShelfNumber = Optional.empty();
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
        chosenShelfNumber = Optional.of(shelfNumber);
        updateDisplay();
    }

    public void insert(Coin coin) {
        coinsForChosenProduct.add(coin);
        if (areEnoughCoinsInsertedToBuyChosenProduct()) {
            Optional<List<Coin>> takenCoins =
                coinsForChosenProduct.takeEquivalentOf(priceOfProductAtShelf(chosenShelfNumber).get());
            if (takenCoins.isPresent()) {
                takeOutTray.put(productAtShelf(chosenShelfNumber).get());
                lastTryWasSuccessful = true;
            } else {
                lastTryWasSuccessful = false;
            }
            coinsForChosenProduct.takeAll()
                .stream()
                .forEach(takenCoin -> returnedCoinsTray.put(takenCoin));
            chosenShelfNumber = Optional.empty();
        }
        updateDisplay();
    }

    public void cancel() {
        chosenShelfNumber = Optional.empty();
        coinsForChosenProduct.asList()
            .stream()
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
            Optional<Cost> optionalProductPrice = priceOfProductAtShelf(chosenShelfNumber);
            Cost remainingCost = optionalProductPrice.get().subtract(coinsForChosenProduct.value());
            display.promptForMoneyToInsert(remainingCost);
        }
    }

    private boolean isProductChosen() {
        return chosenShelfNumber.isPresent();
    }

    private boolean areEnoughCoinsInsertedToBuyChosenProduct() {
        return priceOfProductAtShelf(chosenShelfNumber)
            .filter(price -> coinsForChosenProduct.value().isEqualOrGreaterThan(price))
            .isPresent();
    }

    private Optional<Cost> priceOfProductAtShelf(Optional<Integer> chosenShelfNumber) {
        return chosenShelfNumber.map(shelfNumber -> shelves.priceOfProductAtShelf(shelfNumber));
    }

    private Optional<Product> productAtShelf(Optional<Integer> chosenShelfNumber) {
        return chosenShelfNumber.map(shelfNumber -> shelves.productAtShelf(shelfNumber));
    }
}
