package pl.nkoder.katas.vendingmachine.memory;

import pl.nkoder.katas.vendingmachine.money.Cost;

import static pl.nkoder.katas.vendingmachine.money.Cost.costOf;

public class InsertedValueMemory {

    private Cost insertedValue;

    public InsertedValueMemory() {
        clear();
    }

    public void add(Cost value) {
        insertedValue = insertedValue.add(value);
    }

    public Cost value() {
        return insertedValue;
    }

    public void clear() {
        insertedValue = costOf("0");
    }
}
