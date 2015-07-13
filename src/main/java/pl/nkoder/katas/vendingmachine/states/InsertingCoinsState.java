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
    private final Cost insertedCoinsValue;

    public InsertingCoinsState(int chosenShelfNumber, VendingMachineStateContext context) {
        this(costOf("0"), chosenShelfNumber, context);
    }

    private InsertingCoinsState(Cost insertedCoinsValue, int chosenShelfNumber, VendingMachineStateContext context) {
        this.context = context;
        this.insertedCoinsValue = insertedCoinsValue;
        this.chosenShelfNumber = chosenShelfNumber;
    }

    @Override
    public void handleChoiceOfShelfNumber(int shelfNumber) {
        // do nothing
    }

    @Override
    public void handleCoinInsertion(Coin coin) {
        context.addCoin(coin);
        Cost newInsertedValue = insertedCoinsValue.add(coin.value);
        Cost productPrice = context.priceOfProductAtShelf(chosenShelfNumber);
        VendingMachineState nextState;
        if (!newInsertedValue.isGreaterOrEqualTo(productPrice)) {
            nextState = new InsertingCoinsState(newInsertedValue, chosenShelfNumber, context);
        } else {
            Cost change = newInsertedValue.subtract(productPrice);
            Optional<List<Coin>> changeCoins = context.takeCoinsOfValueOf(change);
            if (changeCoins.isPresent()) {
                context.sellProductAtShelf(chosenShelfNumber);
                context.returnCoins(changeCoins.get());
                nextState = new WaitingForShelfChoiceState(context);
            } else {
                Optional<List<Coin>> coins = context.takeCoinsOfValueOf(newInsertedValue);
                context.returnCoins(coins.get());
                nextState = new UnableToReturnChangeState(context);
            }
        }
        context.changeStateTo(nextState);
    }

    @Override
    public void handleCancellation() {
        List<Coin> coinsToReturn = context.takeCoinsOfValueOf(insertedCoinsValue).get();
        context.returnCoins(coinsToReturn);
        context.changeStateTo(new WaitingForShelfChoiceState(context));
    }

    @Override
    public void handleUpdateOf(Display display) {
        Cost productPrice = context.priceOfProductAtShelf(chosenShelfNumber);
        Cost remainingCost = productPrice.subtract(insertedCoinsValue);
        display.promptForMoneyToInsert(remainingCost);
    }

}
