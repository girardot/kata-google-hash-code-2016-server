package google.hash.code.score;

import google.hash.code.model.Position;

public class Action {

    public final ActionType type;
    public final Position position;

    public Action(ActionType type, Position position) {
        this.type = type;
        this.position = position;
    }

    @Override
    public String toString() {
        return "Action{" +
                "type=" + type +
                ", position=" + position +
                '}';
    }
}
