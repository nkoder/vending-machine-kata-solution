package pl.nkoder.katas.vendingmachine.money;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static pl.nkoder.katas.vendingmachine.money.Cost.costOf;

public class Coins {

    private List<Coin> listOfCoins = new ArrayList<>();

    public Coins() {
    }

    Coins(List<Coin> copy) {
        listOfCoins = copyOf(copy);
    }

    public void add(Coin coin) {
        listOfCoins.add(coin);
    }

    public Cost value() {
        return listOfCoins
            .stream()
            .map(coin -> coin.value)
            .reduce(costOf("0"), Cost::add);
    }

    public Optional<List<Coin>> takeEquivalentOf(Cost cost) {
        Optional<Coins> optionalCoins = new EquivalentOf(cost).using(this);
        if (optionalCoins.isPresent()) {
            for (Coin coinToRemove : optionalCoins.get().asList()) {
                listOfCoins.remove(coinToRemove);
            }
            return Optional.of(optionalCoins.get().asList());
        } else {
            return Optional.empty();
        }
    }

    public List<Coin> takeAll() {
        List<Coin> coinsToBeTaken = asList();
        listOfCoins.clear();
        return coinsToBeTaken;
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
