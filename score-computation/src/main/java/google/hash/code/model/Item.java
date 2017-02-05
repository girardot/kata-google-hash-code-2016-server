package google.hash.code.model;

public class Item {

    public final int type;
    public final int count;

    public Item(int type, int count) {
        this.type = type;
        this.count = count;
    }

    @Override
    public String toString() {
        return "Item{" +
                "type=" + type +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return type == item.type && count == item.count;
    }

    @Override
    public int hashCode() {
        int result = type;
        result = 31 * result + count;
        return result;
    }
}
