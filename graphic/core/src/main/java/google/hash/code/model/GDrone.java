package google.hash.code.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import google.hash.code.MainStage;

import java.util.List;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static google.hash.code.score.ActionType.*;


public class GDrone {

    public static final int DRONE_WIDTH = 25;
    public static final int DRONE_HEIGHT = 25;

    private final Group group = new Group();

    private final Action actions;
    private final GPosition initialPosition = new GPosition(0, 0);
    private final Image movingDrone = new Image(new Texture("drone.png"));

    public GDrone(MainStage stage, List<google.hash.code.score.Action> actions, Color color) {

        movingDrone.setSize(DRONE_WIDTH, DRONE_HEIGHT);
        movingDrone.setColor(color);
        group.addActor(movingDrone);

        group.setPosition(this.initialPosition.x, this.initialPosition.y);
        group.setVisible(true);
        stage.addActor(group);

        final SequenceAction sequence = sequence();
        actions.stream().forEach(action -> {
                    if (MOVE.equals(action.type)) {
                        GPosition gPosition = new GPosition(action.position, stage.widthUnit, stage.heightUnit);
                        sequence.addAction(moveTo(gPosition.x, gPosition.y, stage.timePerTurn));
                    } else if (LOAD.equals(action.type) || DELIVER.equals(action.type)) {
                        sequence.addAction(scaleBy(-0.5f, -0.5f, stage.timePerTurn / 2));
                        sequence.addAction(scaleBy(0.5f, 0.5f, stage.timePerTurn / 2));
                    } else {
                        sequence.addAction(delay(stage.timePerTurn));
                    }
                }
        );
        this.actions = Actions.forever(sequence);
    }

    public void start() {
        group.setPosition(initialPosition.x, initialPosition.y);
        group.addAction(actions);
    }
}
