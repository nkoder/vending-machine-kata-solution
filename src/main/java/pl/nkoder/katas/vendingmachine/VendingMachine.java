package pl.nkoder.katas.vendingmachine;

public class VendingMachine {

    private final Shelves shelves;
    private final Display display;

    public VendingMachine(Shelves shelves) {
        this.shelves = shelves;
        this.display = new Display();
        display.promptForProductChoice();
    }

    public String displayedMessage() {
        return display.message();
    }

    public void choose(int shelfNumber) {
        Price price = priceOfProductAtShelf(shelfNumber);
        display.promptForMoneyToInsert(price);
    }

    public void cancel() {
        display.promptForProductChoice();
    }

    private Price priceOfProductAtShelf(int shelfNumber) {
        return shelves.priceOfProductAtShelf(shelfNumber);
    }
}
