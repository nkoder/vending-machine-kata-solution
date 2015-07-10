package pl.nkoder.katas.vendingmachine.display;

import pl.nkoder.katas.vendingmachine.money.Cost;

import static java.lang.String.format;

public class Display {

    private String message;

    public String message() {
        return message;
    }

    public void promptForProductChoice() {
        message = "Choose a product";
    }

    public void promptForMoneyToInsert(Cost cost) {
        message = format("Insert %s", cost.asText());
    }
}
