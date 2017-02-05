package google.hash.code;

public enum InputType {
    SIMPLE("simple"),
    REDUNDANCY("redundancy"),
    MOTHER_OF_ALL_WAREHOUSES("mother_of_all_warehouses"),
    BUSY_DAY("busy_day");

    public final String label;

    InputType(String label) {
        this.label = label;
    }
}
