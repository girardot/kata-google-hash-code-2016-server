package google.hash.code;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.common.collect.Lists;
import google.hash.code.input.InputReader;
import google.hash.code.model.Position;
import google.hash.code.model.World;
import google.hash.code.score.OutputFileReader;
import google.hash.code.score.ScoreDrone;
import google.hash.code.score.ScoreProcessor;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        final List<TeamScore> teams = Lists.newArrayList(new TeamScore("BIBO"), new TeamScore("Test"));

        for (String fileName : fileNames) {
            LOGGER.info("File " + fileName + " loading");
            World world = inputReader.parse("/" + fileName);

            for (TeamScore team : teams) {
                String outFilePath = "c:/Users/juju/Dropbox/code/kata-google-hash-code-2016-server/out/" + team.getName() + "/" + fileName.replace(".in", "");
                if (teamFileExist(outFilePath)) {
                    final List<ScoreDrone> drones = outputFileReader.parse(Paths.get(outFilePath), world);
                    team.getDrones().addAll(drones);
                    LOGGER.info("Score Processing, Actions computing");
                    LOGGER.info("Score => {} ", scoreProcessor.computeScore(world, drones));
                } else {
                    LOGGER.info("Team {} does have the file {}", team.getName(), outFilePath);
                }
            }

            List<Position> warehouses = world.warehouses.stream()
                    .map(warehouse -> warehouse.position)
                    .collect(Collectors.toList());

            new LwjglApplication(
                    new MyGdxGame(warehouses, new ArrayList<>(), world.orders, world.columns, world.rows, world.turns, teams),
                    buildLwjglApplicationConfiguration()
            );
        }

    }

    private static boolean teamFileExist(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    private static LwjglApplicationConfiguration buildLwjglApplicationConfiguration() {
        LwjglApplicationConfiguration lwjglApplicationConfiguration = new LwjglApplicationConfiguration();
        lwjglApplicationConfiguration.width = WIDTH;
        lwjglApplicationConfiguration.height = HEIGHT;
        return lwjglApplicationConfiguration;
    }
}
