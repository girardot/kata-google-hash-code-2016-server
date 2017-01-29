package google.hash.code.score;

import google.hash.code.model.*;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static google.hash.code.model.InstructionType.DELIVER;
import static google.hash.code.model.InstructionType.LOAD;
import static org.slf4j.LoggerFactory.getLogger;

public class ScoreProcessor {
    public final Logger LOGGER = getLogger(ScoreProcessor.class);

    public int computeScore(World world, List<Drone> drones, List<Warehouse> warehousesAtBeginning) {
        final List<ScoreDrone> scoreDrones = drones.stream()
                .map(d -> new ScoreDrone(d.index, d.getInstructions(), world.maxPayLoad, world.productTypeWeigh, warehousesAtBeginning))
                .collect(Collectors.toList());
        return computeScore(world, scoreDrones);
    }

    public int computeScore(World world, List<ScoreDrone> scoreDrones) {
        int score = 0;
        final Set<OrderItem> itemsDone = new HashSet<>();

        for (int turn = 0; turn < world.turns; turn++) {

            LOGGER.debug("\n***************** Turn : " + turn + " *****************");

            for (ScoreDrone drone : scoreDrones) {
                LOGGER.debug("xxxxxxxxxxxxxxxxxxxx Drone: " + drone.index + " xxxxxxxxxxxxxxxxxxxx");

                if (drone.getCurrentInstruction() == null) {
                    LOGGER.debug("Drone(" + drone.index + ") do not have instruction");
                } else {
                    LOGGER.debug("Drone(" + drone.index + ") instruction is : " + drone.getCurrentInstruction());
                    Position instructionDestination = drone.getCurrentInstruction().getDestination();

                    boolean hasMove = drone.moveTo(instructionDestination, drone.getCurrentInstruction().getInstructionType());
                    if (!hasMove) {
                        if (LOAD.equals(drone.getCurrentInstruction().getInstructionType())) {
                            drone.load(drone.getCurrentInstruction());
                        } else if (DELIVER.equals(drone.getCurrentInstruction().getInstructionType())) {
                            drone.deliver(drone.getCurrentInstruction());

                            for (Order order : world.orders) {
                                if (order.equals(drone.getCurrentInstruction().getOrder())) {
                                    order.expectedItems.stream()
                                            .filter(item -> item.type == drone.getCurrentInstruction().getProductType())
                                            .forEach(itemsDone::add);

                                    if (order.expectedItems.stream().allMatch(itemsDone::contains)) {
                                        score += 100 * (world.turns - turn) / world.turns;
                                    }
                                }
                            }
                        }
                        Instruction nextInstruction = drone.getNextInstruction();
                        LOGGER.debug("Drone(" + drone.index + ") new instruction is : " + nextInstruction);
                    }
                }
            }
        }

        return score;
    }

}
