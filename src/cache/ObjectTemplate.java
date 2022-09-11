package cache;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class ObjectTemplate {
    private final int TARGET = 2, PATH = 3, VALUE = 4; // indexes
    LinkedHashMap<String, ValueStructure> floorMap = new LinkedHashMap<>();
    LinkedHashMap<String, ValueStructure> objectDataPointer = floorMap;

    private void recursiveCreation(LinkedList<String> list, int untilIndex) {
        if (list.size() == untilIndex)
            return;

        String path = list.getFirst();
        ValueStructure tempOb;

        if (objectDataPointer.containsKey(path))
            tempOb = objectDataPointer.get(path);
        else {
            tempOb = new ValueStructure();
            objectDataPointer.put(path, tempOb);
        }

        objectDataPointer = tempOb.valueMap;
        list.removeFirst();
        recursiveCreation(list, untilIndex);
    }

    private void recursiveNavigation(LinkedList<String> list) {
        if (list.size() == 1)
            return;

        ValueStructure tempOb;
        String path = list.getFirst();

        if (objectDataPointer.containsKey(path))
            tempOb = objectDataPointer.get(path);
        else {
            System.out.println("Target doesn't exist!");
            return;
        }

        objectDataPointer = tempOb.valueMap;
        list.removeFirst();
        recursiveNavigation(list);
    }

    void create(String[] commands) {
        if (commands.length < VALUE)
            return;

        if (commands[PATH].length() == 0) {
            System.out.println("Missing arguments!");
            return;
        }

        var listOfPaths = createListOfPath(commands[PATH]);

        switch (commands[TARGET]) {
            case "object" -> createObject(listOfPaths);
            case "field" -> {
                if (listOfPaths.size() == 1) {
                    System.out.println("Missing a value or a name of a field! Try cache create field \"map.fieldName\" ");
                    return;
                }

                // Index 4 out of bounds for length 4 to do
                if (commands.length != VALUE + 1 && commands[VALUE].length() == 0) {
                    System.out.println("Missing field value!");
                    return;
                }

                String value = commands[VALUE];
                createField(listOfPaths, value);
            }
            default ->
                    System.out.println("Missing commands. Try using object or field after create : cache create field \"object.field\" \"value\"");
        }
    }

    private void createObject(LinkedList<String> list) {
        recursiveCreation(list, 0);
        objectDataPointer = floorMap; // reset pointer;
    }

    private void createField(LinkedList<String> listOfPaths, String value) {
        recursiveCreation(listOfPaths, 1);
        String key = listOfPaths.getFirst();

        if (objectDataPointer.containsKey(key)) {
            System.out.println("An object or a filed with the same name already exist!");
            objectDataPointer = floorMap;
            return;
        }

        objectDataPointer.put(key, new ValueStructure(value));
        objectDataPointer = floorMap; // reset pointer
    }

    public void display() {
        ValueStructure.resetHierarchyLevel();
        for (Map.Entry i : floorMap.entrySet())
            System.out.print("\"" + i.getKey() + "\" : {\n" + i.getValue());
        System.out.println("}");
    }

    private LinkedList<String> createListOfPath(String path) {
        return new LinkedList<>(List.of(path
                .replace("\"", "")
                .split("\\W")));
    }
}