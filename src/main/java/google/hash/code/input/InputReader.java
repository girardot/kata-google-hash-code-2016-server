package google.hash.code.input;


import google.hash.code.model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class InputReader {

    public World parse(String fileName) throws FileNotFoundException {

        try (Stream<String> lines = Files.lines(Paths.get(InputReader.class.getResource(fileName).toURI()))) {

            List<String> collectedLines = lines.collect(Collectors.toList());

            World world = initWorld(collectedLines.get(0));
            world.productTypeWeigh = retrieveProductTypeWeigh(collectedLines);
            world.warehouses = retrieveWarehouses(collectedLines);
            world.orders = retrieveOrders(collectedLines, world.warehouses.size());

            return world;

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Order> retrieveOrders(List<String> collectedLines, int warehouseSize) {
        int orderPosition = 4 + (warehouseSize * 2);
        int orderCount = parseInt(collectedLines.get(orderPosition++));

        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < orderCount; i++) {

            String positionLine = collectedLines.get(orderPosition++);
            orderPosition++;//skip order count

            int[] itemsLine = Stream.of(collectedLines.get(orderPosition++).split(" ")).mapToInt(Integer::parseInt).toArray();
            orders.add(new Order(i, parsePosition(positionLine), parseOrderItems(itemsLine)));

        }
        return orders;
    }

    private List<OrderItem> parseOrderItems(int[] itemsLine) {
        return IntStream.of(itemsLine)
                .mapToObj(type -> new OrderItem(type, 1))
                .collect(Collectors.toList());
    }

    private List<Warehouse> retrieveWarehouses(List<String> collectedLines) {
        int warehouseCount = parseInt(collectedLines.get(3));

        List<Warehouse> warehouses = new ArrayList<>();
        int wareHouseCursor = 4;
        for (int i = 0; i < warehouseCount; i++) {
            Position position = parsePosition(collectedLines.get(wareHouseCursor++));
            List<Item> items = parseItems(collectedLines.get(wareHouseCursor++));

            Warehouse warehouse = new Warehouse(i, position, items);
            warehouses.add(warehouse);
        }
        return warehouses;
    }

    private List<Item> parseItems(String itemsAsString) {
        List<Item> items = new ArrayList<>();

        String[] split = itemsAsString.split(" ");
        for (int i = 0; i < split.length; i++) {
            int count = parseInt(split[i]);
            if (count > 0) {
                items.add(new Item(i, count));
            }
        }

        return items;
    }

    private Position parsePosition(String positionAsText) {
        String[] split = positionAsText.split(" ");

        return new Position(parseInt(split[0]), parseInt(split[1]));
    }

    private List<Integer> retrieveProductTypeWeigh(List<String> collectedLines) {
        String productTypeWeighLine = collectedLines.get(2);
        String[] weighAsString = productTypeWeighLine.split(" ");

        return Stream.of(weighAsString)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private World initWorld(String line) {

        Pattern pattern = Pattern.compile("(\\d+) (\\d+) (\\d+) (\\d+) (\\d+)");
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            return new World(parseInt(matcher.group(1)), parseInt(matcher.group(2)), parseInt(matcher.group(3)), parseInt(matcher.group(4)), parseInt(matcher.group(5)));
        }

        throw new IllegalArgumentException("unknow format " + line);
    }

}
