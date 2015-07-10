package pl.nkoder.katas.vendingmachine;

import java.util.HashMap;
import java.util.Map;

public class Shelves {

    private final Map<Integer, Shelf> shelvesByNumbers = new HashMap<>();

    public Shelves putProduct(Product product, int shelfNumber, Price price) {
        shelvesByNumbers.put(shelfNumber, new Shelf(product, price));
        return this;
    }

    public Price priceOfProductAtShelf(int shelfNumber) {
        return shelvesByNumbers.get(shelfNumber).price;
    }
}
