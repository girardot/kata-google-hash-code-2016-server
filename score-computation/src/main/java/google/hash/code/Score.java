package google.hash.code;

public class Score {

    private final InputType inputType;
    private final Integer value;

    public Score(InputType inputType, Integer value) {
        this.inputType = inputType;
        this.value = value;
    }

    public InputType getInputType() {
        return inputType;
    }

    public Integer getValue() {
        return value;
    }
}
