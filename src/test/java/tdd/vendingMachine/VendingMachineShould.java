package tdd.vendingMachine;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VendingMachineShould {

    @Test
    public void by_default_ask_for_product_choice() {
        VendingMachine vendingMachine = new VendingMachine();

        String message = vendingMachine.displayedMessage();

        assertThat(message).isEqualTo("Choose a product");
    }
}
