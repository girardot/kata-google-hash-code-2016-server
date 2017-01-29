package google.hash.code.score;

import google.hash.code.model.*;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static google.hash.code.model.InstructionType.LOAD;
import static org.slf4j.LoggerFactory.getLogger;

public class ScoreDrone {
    public final Logger LOGGER = getLogger(ScoreDrone.class);
    private final List<Instruction> instructions;
    private int currentIntruction = 0;
    private Position position = new Position(0, 0);
    private Map<Integer, Integer> itemsCarried = new HashMap<>();

    public final int maxPayLoad;
    private final List<Integer> productWeights;
    private final List<Warehouse> warehousesAtBeginning;
    public final int index;

    public ScoreDrone(int index,
                      List<Instruction> instructions,
                      int maxPayLoad,
                      List<Integer> productWeights,
                      List<Warehouse> warehousesAtBeginning) {
        this.index = index;
        this.maxPayLoad = maxPayLoad;
        this.productWeights = productWeights;
        this.warehousesAtBeginning = warehousesAtBeginning;
        this.instructions = instructions;
    }

    public Instruction getNextInstruction() {
        currentIntruction++;
        return getCurrentInstruction();
    }

    public Instruction getCurrentInstruction() {
        if (currentIntruction < instructions.size()) {
            return instructions.get(currentIntruction);
        }
        return null;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public Map<Integer, Integer> getItemsCarried() {
        return itemsCarried;
    }

    public boolean moveTo(Position destination, InstructionType instructionType) {
        int distanceToDestination = position.distance(destination);
        boolean hasMove = false;
        if (distanceToDestination != 0) {
            LOGGER.debug("Drone(" + index + ") " + position + " is moving to " + (LOAD.equals(instructionType) ? "warehouse" : "client") + destination);
            position = position.moveToDestination(destination);
            LOGGER.debug(" -----> new drone(" + index + ") position is  " + position);
            hasMove = true;
        }
        return hasMove;
    }

    public void load(Instruction instruction) {
        if (canLoadItems(instruction.getProductType(), instruction.getProductNumber())) {
            final Warehouse warehouse = warehousesAtBeginning.stream()
                    .filter(w -> (w.index == instruction.getWareHouse().index)).findFirst()
                    .orElseGet(null);
            final Item itemInWarehouse = warehouse.getItems().stream()
                    .filter(item -> item.type == instruction.getProductType()).findFirst().orElse(new Item(-1, 0));
            final int numberAvailable = itemInWarehouse.count;

            if (numberAvailable >= instruction.getProductNumber()) {
                warehouse.getItems().remove(itemInWarehouse);
                warehouse.getItems().add(new Item(instruction.getProductType(), numberAvailable - instruction.getProductNumber()));
                loadProduct(instruction.getProductType(), instruction.getProductNumber());
                LOGGER.debug("Drone(" + index + ") is loading " + instruction.getProductNumber() + " item(s) " + instruction.getProductType() + " from warehouse " + instruction.getWareHouse().index);
            } else {
                LOGGER.debug("WARNING !!!!!!! Product Not Available in warehouse !!!!!!!!!!!!");
                LOGGER.debug("WARNING !!!!!!! Drone(" + index + ") cannot load " + instruction.getProductNumber() + " item(s) " + instruction.getProductType() + " from warehouse " + instruction.getWareHouse().index);
            }
        } else {
            LOGGER.debug("WARNING !!!!!!! Max payload reached !!!!!!!!!!!!");
            LOGGER.debug("WARNING !!!!!!! Drone(" + index + ") cannot load " + instruction.getProductNumber() + " item(s) " + instruction.getProductType() + " from warehouse " + instruction.getWareHouse().index);
        }
    }

    public void deliver(Instruction instruction) {
        if (hasProducts(instruction.getProductType(), instruction.getProductNumber())) {
            unloadProduct(instruction.getProductType(), instruction.getProductNumber());
            LOGGER.debug("Drone(" + index + ") is deliver item " + instruction.getProductType() + " to order " + instruction.getOrder().index);
        } else {
            LOGGER.debug("WARNING !!!!!!! Deliver failed !!!!!!!!!!!!");
            LOGGER.debug("WARNING !!!!!!! Drone(" + index + ") does not has " + instruction.getProductNumber() + " " + instruction.getProductType());
        }
    }

    private boolean hasProducts(int productType, int productNumber) {
        return itemsCarried.getOrDefault(productType, 0) >= productNumber;
    }

    private void loadProduct(int productType, int numberToLoad) {
        itemsCarried.put(
                productType,
                itemsCarried.getOrDefault(productType, 0) + numberToLoad
        );
    }

    private void unloadProduct(int productType, int numberToUnload) {
        loadProduct(productType, (-1 * numberToUnload));
    }

    private boolean canLoadItems(int productType, int productNumber) {
        return productNumber * productWeights.get(productType) + itemsCarriedWeight() < maxPayLoad;
    }

    private int itemsCarriedWeight() {
        int totalCarriedWeight = 0;
        for (Map.Entry<Integer, Integer> entry : itemsCarried.entrySet()) {
            totalCarriedWeight = productWeights.get(entry.getKey()) * entry.getValue();
        }
        return totalCarriedWeight;
    }
}
