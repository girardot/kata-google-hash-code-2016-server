package google.hash.code.model;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import google.hash.code.MainStage;

import java.util.stream.IntStream;

import static com.badlogic.gdx.Gdx.graphics;
import static com.badlogic.gdx.graphics.Color.BLUE;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class GTurn {

    private final Label label;
    private static final Label.LabelStyle textStyle = new Label.LabelStyle(new BitmapFont(), BLUE);
    private final Action actions;

    public GTurn(MainStage stage, int totalTurn) {
        label = new Label("0/" + totalTurn, textStyle);

        label.setPosition(
                (graphics.getWidth() / 2 - label.getText().length() / 2),
                graphics.getHeight() - 20
        );
        stage.addActor(label);

        final SequenceAction sequence = sequence();
        IntStream.range(0, totalTurn).forEach(
                i -> {
                    sequence.addAction(run(() -> label.setText(i + "/" + totalTurn)));
                    sequence.addAction(delay(stage.timePerTurn));
                }
        );

        this.actions = forever(sequence);
    }

    public void start() {
        label.addAction(actions);
    }
}
