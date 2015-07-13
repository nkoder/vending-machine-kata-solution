package pl.nkoder.katas.vendingmachine.states;

import pl.nkoder.katas.vendingmachine.display.Display;
import pl.nkoder.katas.vendingmachine.money.Coin;

public class WaitingForShelfChoiceState implements VendingMachineState {

    private final VendingMachineStateContext context;

    public WaitingForShelfChoiceState(VendingMachineStateContext context) {
        this.context = context;
    }

    @Override
    public void handleChoiceOfShelfNumber(int shelfNumber) {
        context.changeStateTo(new InsertingCoinsState(shelfNumber, context));
    }

    @Override
    public void handleCoinInsertion(Coin coin) {
        // TODO
    }

    @Override
    public void handleCancellation() {
        // do nothing
    }

    @Override
    public void handleUpdateOf(Display display) {
        display.promptForProductChoice();
    }
}
