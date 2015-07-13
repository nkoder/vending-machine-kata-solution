package pl.nkoder.katas.vendingmachine.states;

import pl.nkoder.katas.vendingmachine.display.Display;
import pl.nkoder.katas.vendingmachine.money.Coin;

import java.time.Duration;

public class UnableToReturnChangeState implements VendingMachineState {

    private static final Duration DURATION_OF_WARNING_MESSAGE = Duration.ofSeconds(3);

    public UnableToReturnChangeState(VendingMachineStateContext context) {
        context
            .after(DURATION_OF_WARNING_MESSAGE)
            .perform(() -> context.changeStateTo(new WaitingForShelfChoiceState(context)));
    }

    @Override
    public void handleChoiceOfShelfNumber(int shelfNumber) {
        // do nothing
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
        display.warnAboutNoChange();
    }

}
