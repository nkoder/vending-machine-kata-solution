package pl.nkoder.katas.vendingmachine.memory;

import java.util.Optional;

public class ShelfChoiceMemory {

    private Optional<Integer> chosenShelfNumber;

    public void setChosenShelfNumberTo(int shelfNumber) {
        this.chosenShelfNumber = Optional.of(shelfNumber);
    }

    public Optional<Integer> chosenShelf() {
        return chosenShelfNumber;
    }

    public void clear() {
        chosenShelfNumber = Optional.empty();
    }
}
