package pl.nkoder.katas.vendingmachine;

import pl.nkoder.katas.vendingmachine.display.Display;
import pl.nkoder.katas.vendingmachine.money.Coin;
import pl.nkoder.katas.vendingmachine.money.Coins;
import pl.nkoder.katas.vendingmachine.money.Cost;
import pl.nkoder.katas.vendingmachine.shelves.Shelves;

import java.util.List;
import java.util.Optional;

public class VendingMachine {

    private final Shelves shelves;
    private final Display display = new Display();
    private final Coins coins = new Coins();

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
        coins.add(coin);
        updateDisplay();
    }

    public void cancel() {
        chosenShelfNumber = Optional.empty();
        updateDisplay();
    }

    private void updateDisplay() {
        if (chosenShelfNumber.isPresent()) {
            Cost productPrice = priceOfProductAtShelf(chosenShelfNumber.get());
            Cost remainingCost = productPrice.subtract(coins.value());
            display.promptForMoneyToInsert(remainingCost);
        } else {
            display.promptForProductChoice();
        }
    }

    private Cost priceOfProductAtShelf(int shelfNumber) {
        return shelves.priceOfProductAtShelf(shelfNumber);
    }

    public List<Coin> returnedCoins() {
        return coins.asList();
    }
}
