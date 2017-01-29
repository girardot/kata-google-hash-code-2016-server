package google.hash.code.model;

import java.util.List;

public class Warehouse {

    private final List<Item> items;
    public final int index;
    public final Position position;

    public Warehouse(int index, Position position, List<Item> items) {
        this.index = index;
        this.items = items;
        this.position = position;
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "items=" + items +
                ", position=" + position +
                '}';
    }

    public List<Item> getItems() {
        return items;
    }
}
