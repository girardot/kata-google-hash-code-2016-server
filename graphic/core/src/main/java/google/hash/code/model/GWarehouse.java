package google.hash.code.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import google.hash.code.MainStage;

public class GWarehouse {
    public static final int WAREHOUSE_WIDTH = 25;
    public static final int WAREHOUSE_HEIGHT = 25;
    private static final Texture texture = new Texture("warehouse.png");
    private final Image image = new Image(texture);

    public GWarehouse(MainStage stage, GPosition position) {
        image.setPosition(position.x, position.y);
        image.setSize(WAREHOUSE_WIDTH, WAREHOUSE_HEIGHT);
        stage.addActor(image);
    }
}

