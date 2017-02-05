package google.hash.code.input;

import google.hash.code.model.*;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;


public class InputReaderTest {

    @Test
    public void should_parse_input_file() throws FileNotFoundException {
        // Given
        InputReader inputReader = new InputReader();
        String fileName = "/simple.in";

        // When
        World world = inputReader.parse(fileName);

        // Then
        assertThat(world.rows).isEqualTo(100);
        assertThat(world.columns).isEqualTo(100);
        assertThat(world.drones).isEqualTo(3);
        assertThat(world.turns).isEqualTo(50);
        assertThat(world.maxPayLoad).isEqualTo(500);

        assertThat(world.productTypeWeigh).containsExactly(100, 5, 450);

        assertThat(world.warehouses).hasSize(2);
        Warehouse firstWarehouse = world.warehouses.get(0);
        assertThat(firstWarehouse.position).isEqualTo(new Position(0, 0));
        assertThat(firstWarehouse.getItems()).containsOnly(new Item(0, 5), new Item(1, 1));

        Warehouse secondWarehouse = world.warehouses.get(1);
        assertThat(secondWarehouse.position).isEqualTo(new Position(5, 5));
        assertThat(secondWarehouse.getItems()).containsOnly(new Item(1, 10), new Item(2, 2));

        assertThat(world.orders).hasSize(3);

        Order firstOrder = world.orders.get(0);
        assertThat(firstOrder.destination).isEqualTo(new Position(1, 1));
        assertThat(firstOrder.expectedItems).hasSize(2);
        assertThat(firstOrder.expectedItems.get(0)).isEqualTo(new OrderItem(2, 1));
        assertThat(firstOrder.expectedItems.get(1)).isEqualTo(new OrderItem(0, 1));

    }

}