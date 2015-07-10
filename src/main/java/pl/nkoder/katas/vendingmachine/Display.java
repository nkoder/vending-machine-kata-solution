package pl.nkoder.katas.vendingmachine;

import static java.lang.String.format;

public class Display {

    private String message;

    public String message() {
        return message;
    }

    public void promptForProductChoice() {
        message = "Choose a product";
    }

    public void promptForMoneyToInsert(Price price) {
        message = format("Insert %s", price.asText());
    }
}
