package google.hash.code;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import google.hash.code.model.Order;
import google.hash.code.model.Position;
import google.hash.code.score.ScoreDrone;

import java.util.List;


public class MyGdxGame extends ApplicationAdapter {

    private final List<Position> warehouses;
    private final List<ScoreDrone> drones;
    private final List<Order> orders;
    private final int width;
    private final int height;
    private final int totalTurn;
    private final List<TeamScore> teams;

    private MainStage mainStage;

    public MyGdxGame(List<Position> warehouses,
                     List<ScoreDrone> drones,
                     List<Order> orders,
                     int width,
                     int height,
                     int totalTurn,
                     List<TeamScore> teams) {
        this.warehouses = warehouses;
        this.drones = drones;
        this.orders = orders;
        this.width = width;
        this.height = height;
        this.totalTurn = totalTurn;
        this.teams = teams;
    }

    @Override
    public void create() {
        mainStage = new MainStage(width, height, warehouses, drones, orders, totalTurn, teams);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainStage.act();
        mainStage.draw();
    }

    @Override
    public void dispose() {
        mainStage.dispose();
    }

}
