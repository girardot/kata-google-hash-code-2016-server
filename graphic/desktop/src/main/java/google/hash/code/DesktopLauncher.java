package google.hash.code;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import google.hash.code.input.InputReader;
import google.hash.code.model.Position;
import google.hash.code.model.World;
import google.hash.code.score.OutputFileReader;
import google.hash.code.score.ScoreDrone;
import google.hash.code.score.ScoreProcessor;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class DesktopLauncher {

    public static final Logger LOGGER = getLogger(DesktopLauncher.class);
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;

    public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
        final InputReader inputReader = new InputReader();
        final OutputFileReader outputFileReader = new OutputFileReader();
        final ScoreProcessor scoreProcessor = new ScoreProcessor();

        final String[] fileNames = {"simple.in"};
//        final String[] fileNames = {"simple.in", "busy_day.in", "redundancy.in", "mother_of_all_warehouses.in"};

        for (String fileName : fileNames) {
            LOGGER.info("File " + fileName + " loading");
            World world = inputReader.parse("/" + fileName);
            final List<ScoreDrone> drones = outputFileReader.parse("/" + fileName.replace(".in", ".out"), world);

            LOGGER.info("Score Processing, Actions computing");
            LOGGER.info("Score => " + scoreProcessor.computeScore(world, drones));

            List<Position> warehouses = world.warehouses.stream()
                    .map(warehouse -> warehouse.position)
                    .collect(Collectors.toList());

            new LwjglApplication(
                    new MyGdxGame(warehouses, drones, world.orders, world.columns, world.rows),
                    buildLwjglApplicationConfiguration()
            );
        }

    }

    private static LwjglApplicationConfiguration buildLwjglApplicationConfiguration() {
        LwjglApplicationConfiguration lwjglApplicationConfiguration = new LwjglApplicationConfiguration();
        lwjglApplicationConfiguration.width = WIDTH;
        lwjglApplicationConfiguration.height = HEIGHT;
        return lwjglApplicationConfiguration;
    }
}
