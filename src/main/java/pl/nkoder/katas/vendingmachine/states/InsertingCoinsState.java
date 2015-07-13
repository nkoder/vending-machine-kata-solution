package pl.nkoder.katas.vendingmachine.states;

import pl.nkoder.katas.vendingmachine.display.Display;
import pl.nkoder.katas.vendingmachine.money.Coin;
import pl.nkoder.katas.vendingmachine.money.Cost;

import java.util.List;
import java.util.Optional;

import static pl.nkoder.katas.vendingmachine.money.Cost.costOf;

public class InsertingCoinsState implements VendingMachineState {

    private final VendingMachineStateContext context;
    private final int chosenShelfNumber;

    private Cost insertedValue = costOf("0");

    public InsertingCoinsState(int chosenShelfNumber, VendingMachineStateContext context) {
        this.chosenShelfNumber = chosenShelfNumber;
        this.context = context;
    }

    @Override
    public void handleChoiceOfShelfNumber(int shelfNumber) {
        // do nothing
    }

    @Override
    public void handleCoinInsertion(Coin coin) {
        insertedValue = insertedValue.add(coin.value);
        context.allCoins().add(coin);
        if (areEnoughCoinsInsertedToBuyChosenProduct()) {
            Cost change = insertedValue.subtract(context.priceOfProductAtShelf(chosenShelfNumber));
            Optional<List<Coin>> changeCoins = context.allCoins().takeEquivalentOf(change);
            if (changeCoins.isPresent()) {
                context.sellProductAtShelf(chosenShelfNumber);
                context.returnCoins(changeCoins.get());
                context.changeStateTo(new WaitingForShelfChoiceState(context));
            } else {
                Optional<List<Coin>> coins = context.allCoins().takeEquivalentOf(insertedValue);
                context.returnCoins(coins.get());
                context.changeStateTo(new UnableToReturnChangeState(context));
            }
        }
    }

    @Override
    public void handleCancellation() {
        List<Coin> coinsToReturn = context.allCoins().takeEquivalentOf(insertedValue).get();
        context.returnCoins(coinsToReturn);
        context.changeStateTo(new WaitingForShelfChoiceState(context));
    }

    @Override
    public void handleUpdateOf(Display display) {
        Cost productPrice = context.priceOfProductAtShelf(chosenShelfNumber);
        Cost remainingCost = productPrice.subtract(insertedValue);
        display.promptForMoneyToInsert(remainingCost);
    }

    private boolean areEnoughCoinsInsertedToBuyChosenProduct() {
        Cost price = context.priceOfProductAtShelf(chosenShelfNumber);
        return insertedValue.isGreaterOrEqualTo(price);
    }

}
