package google.hash.code;

import google.hash.code.input.InputReader;
import google.hash.code.model.World;
import google.hash.code.score.OutputFileReader;
import google.hash.code.score.ScoreDrone;
import google.hash.code.score.ScoreProcessor;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class Main {
    public static final Logger LOGGER = getLogger(Main.class);

    public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
        final InputReader inputReader = new InputReader();
        final OutputFileReader outputFileReader = new OutputFileReader();
        final ScoreProcessor scoreProcessor = new ScoreProcessor();
        final String[] fileNames = {"simple.in"};
//        String[] fileNames = {"busy_day.in", "redundancy.in", "mother_of_all_warehouses.in"};

        for (String fileName : fileNames) {
            LOGGER.info("File " + fileName + " loading");
            World world = inputReader.parse("/" + fileName);
            final List<ScoreDrone> drones = outputFileReader.parse("/" + fileName.replace(".in", ".out"), world);
            LOGGER.info("Score Processing");
            LOGGER.info("Score => " + scoreProcessor.computeScore(world, drones));
        }
    }
}
