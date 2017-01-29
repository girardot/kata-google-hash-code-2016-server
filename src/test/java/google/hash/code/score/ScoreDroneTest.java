package google.hash.code.score;


import google.hash.code.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static google.hash.code.model.Instruction.buildDeliverInstruction;
import static google.hash.code.model.Instruction.buildLoadInstruction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScoreDroneTest {

    final private int PRODUCT_TYPE = 0;
    final private int OTHER_PRODUCT_TYPE = 1;
    private final Warehouse warehouse = mock(Warehouse.class);
    private final ArrayList<Warehouse> warehousesAtBeginning = newArrayList(
            warehouse
    );

    @Test
    public void should_load_items() {
        // Given
        ScoreDrone scoreDrone = new ScoreDrone(0, new ArrayList<>(), 100, newArrayList(10, 20, 30), warehousesAtBeginning);
        myWarehouseHas(newArrayList(new Item(OTHER_PRODUCT_TYPE, 1)));

        // When
        scoreDrone.load(buildLoadInstruction(warehouse, OTHER_PRODUCT_TYPE, 1));

        //Then
        assertThat(scoreDrone.getItemsCarried()).hasSize(1);
    }

    @Test
    public void should_not_load_items_when_is_too_heavy() {
        // Given
        ScoreDrone scoreDrone = new ScoreDrone(0, new ArrayList<>(), 100, newArrayList(10, 20, 30), warehousesAtBeginning);
        myWarehouseHas(newArrayList(new Item(OTHER_PRODUCT_TYPE, 10)));

        // When
        scoreDrone.load(buildLoadInstruction(warehouse, OTHER_PRODUCT_TYPE, 10));

        //Then
        assertThat(scoreDrone.getItemsCarried()).isEmpty();
    }

    @Test
    public void should_not_load_items_when_total_carried_is_to_heavy() {
        // Given
        ScoreDrone scoreDrone = new ScoreDrone(0, new ArrayList<>(), 100, newArrayList(10, 20, 30), warehousesAtBeginning);
        myWarehouseHas(newArrayList(new Item(PRODUCT_TYPE, 8), new Item(OTHER_PRODUCT_TYPE, 1)));
        scoreDrone.load(buildLoadInstruction(warehouse, PRODUCT_TYPE, 8));
        assertThat(scoreDrone.getItemsCarried().get(PRODUCT_TYPE)).isEqualTo(8);
        assertThat(scoreDrone.getItemsCarried()).hasSize(1);

        // When
        scoreDrone.load(buildLoadInstruction(warehouse, OTHER_PRODUCT_TYPE, 1));

        //Then
        assertThat(scoreDrone.getItemsCarried()).hasSize(1);
        assertThat(scoreDrone.getItemsCarried().get(OTHER_PRODUCT_TYPE)).isNull();
    }

    @Test
    public void should_deliver_loaded_items() {
        // Given
        ScoreDrone scoreDrone = new ScoreDrone(0, new ArrayList<>(), 100, newArrayList(10, 20, 30), warehousesAtBeginning);
        myWarehouseHas(newArrayList(new Item(PRODUCT_TYPE, 8)));
        scoreDrone.load(buildLoadInstruction(warehouse, PRODUCT_TYPE, 8));
        assertThat(scoreDrone.getItemsCarried().get(PRODUCT_TYPE)).isEqualTo(8);
        assertThat(scoreDrone.getItemsCarried()).hasSize(1);

        // When
        scoreDrone.deliver(buildDeliverInstruction(
                new Order(0, new Position(1, 1), newArrayList(new OrderItem(PRODUCT_TYPE, 2))),
                PRODUCT_TYPE,
                2
        ));

        //Then
        assertThat(scoreDrone.getItemsCarried().get(PRODUCT_TYPE)).isEqualTo(6);
        assertThat(scoreDrone.getItemsCarried()).hasSize(1);
    }

    @Test
    public void deliver_only_loaded_items() {
        // Given
        ScoreDrone scoreDrone = new ScoreDrone(0, new ArrayList<>(), 100, newArrayList(10, 20, 30), warehousesAtBeginning);
        myWarehouseHas(newArrayList(new Item(PRODUCT_TYPE, 8)));
        scoreDrone.load(buildLoadInstruction(warehouse, PRODUCT_TYPE, 8));
        assertThat(scoreDrone.getItemsCarried().get(PRODUCT_TYPE)).isEqualTo(8);
        assertThat(scoreDrone.getItemsCarried().get(OTHER_PRODUCT_TYPE)).isNull();
        assertThat(scoreDrone.getItemsCarried()).hasSize(1);

        // When
        scoreDrone.deliver(buildDeliverInstruction(
                new Order(0, new Position(1, 1), newArrayList(new OrderItem(OTHER_PRODUCT_TYPE, 3))),
                OTHER_PRODUCT_TYPE,
                3
        ));

        //Then
        assertThat(scoreDrone.getItemsCarried().get(PRODUCT_TYPE)).isEqualTo(8);
        assertThat(scoreDrone.getItemsCarried().get(OTHER_PRODUCT_TYPE)).isNull();
        assertThat(scoreDrone.getItemsCarried()).hasSize(1);
    }

    @Test
    public void load_product_from_warehouse() {
        // Given
        ScoreDrone scoreDrone = new ScoreDrone(0, new ArrayList<>(), 100, newArrayList(10, 20, 30), warehousesAtBeginning);
        myWarehouseHas(newArrayList(new Item(PRODUCT_TYPE, 8)));

        // When
        scoreDrone.load(buildLoadInstruction(warehouse, PRODUCT_TYPE, 4));

        //Then
        assertThat(scoreDrone.getItemsCarried().get(PRODUCT_TYPE)).isEqualTo(4);
    }

    @Test
    public void do_not_load_when_warehouse_has_not_enough_product_available() {
        // Given
        ScoreDrone scoreDrone = new ScoreDrone(0, new ArrayList<>(), 100, newArrayList(10, 20, 30), warehousesAtBeginning);
        myWarehouseHas(newArrayList(new Item(PRODUCT_TYPE, 8)));

        // When
        scoreDrone.load(buildLoadInstruction(warehouse, PRODUCT_TYPE, 12));

        //Then
        assertThat(scoreDrone.getItemsCarried().get(PRODUCT_TYPE)).isNull();
    }

    @Test
    public void do_not_load_when_product_is_not_available_in_warehouse() {
        // Given
        ScoreDrone scoreDrone = new ScoreDrone(0, new ArrayList<>(), 100, newArrayList(10, 20, 30), warehousesAtBeginning);
        myWarehouseHas(newArrayList(new Item(PRODUCT_TYPE, 8), new Item(OTHER_PRODUCT_TYPE, 5)));

        // When
        scoreDrone.load(buildLoadInstruction(warehouse, 2, 10));

        //Then
        assertThat(scoreDrone.getItemsCarried().get(2)).isNull();
    }

    private void myWarehouseHas(List<Item> items) {
        when(warehouse.getItems()).thenReturn(items);
    }
}