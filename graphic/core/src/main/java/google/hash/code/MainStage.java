package google.hash.code;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import google.hash.code.model.GCustomer;
import google.hash.code.model.GDrone;
import google.hash.code.model.GPosition;
import google.hash.code.model.GWarehouse;
import google.hash.code.model.*;
import google.hash.code.score.ScoreDrone;

import java.util.List;
import java.util.stream.Collectors;

public class MainStage extends Stage {

    public final float widthUnit;
    public final float heightUnit;

    private final List<GCustomer> customers;
    private final List<GDrone> drones;
    private final List<GWarehouse> warehouses;

    public static final Texture texture = new Texture("map.png");
    private static final Image background = new Image(texture);

    public MainStage(int width, int height, List<Position> warehouses, List<ScoreDrone> drones, List<Order> orders) {
        widthUnit = Gdx.graphics.getWidth() / width;
        heightUnit = Gdx.graphics.getHeight() / height;

        background.setPosition(0, 0);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        addActor(background);

        this.warehouses = warehouses
                .stream()
                .map(position -> new GWarehouse(this, new GPosition(position, this.widthUnit, this.heightUnit)))
                .collect(Collectors.toList());

        this.customers = orders
                .stream()
                .map(order -> new GCustomer(this, new GPosition(order.destination, this.widthUnit, this.heightUnit)))
                .collect(Collectors.toList());

        this.drones = drones
                .stream()
                .map(drone -> new GDrone(this, drone.getActions()))
                .collect(Collectors.toList());

        this.drones.forEach(GDrone::start);
    }
}