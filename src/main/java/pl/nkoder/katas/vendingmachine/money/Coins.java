package pl.nkoder.katas.vendingmachine.money;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static pl.nkoder.katas.vendingmachine.money.Cost.costOf;

public class Coins {

    private List<Coin> listOfCoins = new ArrayList<>();

    public void add(Coin coin) {
        listOfCoins.add(coin);
    }

    public Cost value() {
        return listOfCoins
            .stream()
            .map(coin -> coin.value)
            .reduce(costOf("0"), Cost::add);
    }

    public List<Coin> asList() {
        return copyOf(listOfCoins);
    }

    private List<Coin> copyOf(List<Coin> coins) {
        return coins
            .stream()
            .map(coin -> coin)
            .collect(toList());
    }
}
