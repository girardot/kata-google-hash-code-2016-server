package google.hash.code.model;

import java.util.List;

public class Order {

    public final int index;
    public final Position destination;
    public final List<OrderItem> expectedItems;

    public Order(int index, Position destination, List<OrderItem> expectedItems) {
        this.index = index;
        this.destination = destination;
        this.expectedItems = expectedItems;
    }

    @Override
    public String toString() {
        return "Order{" +
                "index=" + index +
                ", destination=" + destination +
                ", expectedItems=" + expectedItems +
                '}';
    }
}
