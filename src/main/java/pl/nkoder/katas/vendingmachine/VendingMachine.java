package pl.nkoder.katas.vendingmachine;

import pl.nkoder.katas.vendingmachine.display.Display;
import pl.nkoder.katas.vendingmachine.money.Coin;
import pl.nkoder.katas.vendingmachine.money.Coins;
import pl.nkoder.katas.vendingmachine.money.Cost;
import pl.nkoder.katas.vendingmachine.products.Product;
import pl.nkoder.katas.vendingmachine.shelves.Shelves;
import pl.nkoder.katas.vendingmachine.tray.Tray;

import java.util.Optional;

public class VendingMachine {

    private final Shelves shelves;
    private final Display display = new Display();
    private final Tray<Product> takeOutTray = new Tray<>();
    private final Tray<Coin> returnedCoinsTray = new Tray<>();

    private Coins coinsForChosenProduct = new Coins();

    private Optional<Integer> chosenShelfNumber = Optional.empty();

    public VendingMachine(Shelves shelves) {
        this.shelves = shelves;
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
            coinsForChosenProduct = new Coins();
            takeOutTray.put(productAtShelf(chosenShelfNumber).get());
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
            display.promptForProductChoice();
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
            .filter(cost -> cost.isEqualTo(coinsForChosenProduct.value()))
            .isPresent();
    }

    private Optional<Cost> priceOfProductAtShelf(Optional<Integer> chosenShelfNumber) {
        return chosenShelfNumber.map(shelfNumber -> shelves.priceOfProductAtShelf(shelfNumber));
    }

    private Optional<Product> productAtShelf(Optional<Integer> chosenShelfNumber) {
        return chosenShelfNumber.map(shelfNumber -> shelves.productAtShelf(shelfNumber));
    }
}
