package google.hash.code.score;

import google.hash.code.input.InputReader;
import google.hash.code.model.Instruction;
import google.hash.code.model.World;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static google.hash.code.model.Instruction.buildDeliverInstruction;
import static google.hash.code.model.Instruction.buildLoadInstruction;
import static java.lang.Integer.parseInt;

public class OutputFileReader {

    public List<ScoreDrone> parse(String outputFileName, World world) throws FileNotFoundException {
        List<ScoreDrone> drones = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(InputReader.class.getResource(outputFileName).toURI()))) {

            List<String> collectedLines = lines.skip(1).collect(Collectors.toList());
            collectedLines
                    .stream()
                    .map(s -> s.split(" "))
                    .forEach(strings -> {
                        final int droneIndex = parseInt(strings[0]);
                        final String instructionType = strings[1];
                        final int warehouseOrOrder = parseInt(strings[2]);
                        final int productType = parseInt(strings[3]);
                        final int productQuantity = parseInt(strings[4]);

                        final ScoreDrone scoreDrone = drones.stream()
                                .filter(drone -> drone.index == droneIndex)
                                .findAny()
                                .orElse(
                                        new ScoreDrone(
                                                droneIndex,
                                                new ArrayList<>(),
                                                world.maxPayLoad,
                                                world.productTypeWeigh,
                                                world.warehouses
                                        ));

                        if (!drones.contains(scoreDrone)) {
                            drones.add(scoreDrone);
                        }

                        Instruction instruction = null;
                        if ("L".equals(instructionType)) {
                            instruction = buildLoadInstruction(world.warehouses.get(warehouseOrOrder), productType, productQuantity);
                        } else if ("D".equals(instructionType)) {
                            instruction = buildDeliverInstruction(world.orders.get(warehouseOrOrder), productType, productQuantity);
                        }

                        scoreDrone.getInstructions().add(instruction);
                    });


        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return drones;
    }
}

