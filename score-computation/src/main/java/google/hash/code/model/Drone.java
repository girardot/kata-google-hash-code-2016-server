package google.hash.code.model;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static google.hash.code.model.Instruction.buildDeliverInstruction;
import static google.hash.code.model.Instruction.buildLoadInstruction;

public class Drone {

    public final int index;

    private final List<Instruction> instructions;

    public Drone(int index) {
        this.index = index;
        this.instructions = new ArrayList<>();
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void load(int productType, int numberProduct, Warehouse warehouse) {
        instructions.add(buildLoadInstruction(warehouse, productType, numberProduct));
    }

    public void deliver(int productType, int numberProduct, Order order) {
        instructions.add(buildDeliverInstruction(order, productType, numberProduct));
    }

    public void write(Writer writer) throws IOException {
        for (Instruction instruction : instructions) {
            instruction.write(index, writer);
        }
    }
}
