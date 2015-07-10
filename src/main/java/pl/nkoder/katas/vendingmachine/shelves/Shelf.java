package pl.nkoder.katas.vendingmachine.shelves;

import pl.nkoder.katas.vendingmachine.money.Cost;
import pl.nkoder.katas.vendingmachine.products.Product;

public class Shelf {

    public final Product product;
    public final Cost price;

    public Shelf(Product product, Cost price) {
        this.product = product;
        this.price = price;
    }
}
