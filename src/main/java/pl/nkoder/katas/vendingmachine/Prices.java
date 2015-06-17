package pl.nkoder.katas.vendingmachine;

import java.util.HashMap;
import java.util.Map;

public class Prices {

    private final Map<Product, Price> pricesOfProducts = new HashMap<>();

    public void addPrice(Product product, Price price) {
        pricesOfProducts.put(product, price);
    }

    public Price priceOf(Product product) {
        return pricesOfProducts.get(product);
    }
}
