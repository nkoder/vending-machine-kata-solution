package pl.nkoder.katas.vendingmachine;

public class VendingMachine {

    private final Prices prices;
    private String message;

    public VendingMachine(Prices prices) {
        this.prices = prices;
        message = "Choose a product";
    }

    public String displayedMessage() {
        return message;
    }

    public void choose(Product product) {
        message = "Insert " + prices.priceOf(product).asText();
    }
}
