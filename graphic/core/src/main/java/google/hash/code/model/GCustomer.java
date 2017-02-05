package google.hash.code.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import google.hash.code.MainStage;

public class GCustomer {

    public static final int CUSTOMER_WIDTH = 20;
    public static final int CUSTOMER_HEIGHT = 20;
    private static final Texture texture = new Texture("customer.png");
    private final Image image = new Image(texture);

    public GCustomer(MainStage stage, GPosition position) {
        image.setPosition(position.x, position.y);
        image.setSize(CUSTOMER_WIDTH, CUSTOMER_HEIGHT);
        stage.addActor(image);
    }
}
