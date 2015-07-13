package pl.nkoder.katas.vendingmachine.states;

import pl.nkoder.katas.vendingmachine.display.Display;
import pl.nkoder.katas.vendingmachine.money.Coin;

public interface VendingMachineState {

    void handleChoiceOfShelfNumber(int shelfNumber);

    void handleCoinInsertion(Coin coin);

    void handleCancellation();

    void handleUpdateOf(Display display);

}
