package pl.nkoder.katas.vendingmachine.money;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

public class EquivalentOf {

    private final Cost cost;

    public EquivalentOf(Cost cost) {
        this.cost = cost;
    }

    public Optional<Coins> using(Coins coins) {
        for (Coin coin : coins.asList()) {
            if (cost.isEqualTo(coin.value)) {
                return Optional.of(new Coins(newArrayList(coin)));
            }
            if (cost.isGreaterThan(coin.value)) {
                Coins restOfCoins = subtract(coins, coin);
                Cost restOfCost = cost.subtract(coin.value);
                Optional<Coins> restOfEquivalent = new EquivalentOf(restOfCost).using(restOfCoins);
                if (restOfEquivalent.isPresent()) {
                    Coins coinsEquivalentToCost = add(restOfEquivalent.get(), coin);
                    return Optional.of(coinsEquivalentToCost);
                }
            }
        }
        return Optional.empty();
    }

    private Coins add(Coins coins, Coin coin) {
        List<Coin> result = coins.asList();
        result.add(coin);
        return new Coins(result);
    }

    private Coins subtract(Coins coins, Coin coinToRemove) {
        List<Coin> result = coins.asList();
        result.remove(coinToRemove);
        return new Coins(result);
    }

}
