package pl.nkoder.katas.vendingmachine;

public class Shelf {

    public final Product product;
    public final Price price;

    public Shelf(Product product, Price price) {
        this.product = product;
        this.price = price;
    }
}
