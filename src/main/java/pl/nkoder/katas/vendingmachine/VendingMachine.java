package pl.nkoder.katas.vendingmachine;

public class VendingMachine {

    private final Shelves shelves;
    private String message;

    public VendingMachine(Shelves shelves) {
        this.shelves = shelves;
        message = "Choose a product";
    }

    public String displayedMessage() {
        return message;
    }

    public void choose(int shelfNumber) {
        message = "Insert " + shelves.priceOfProductAtShelf(shelfNumber).asText();
    }
}
