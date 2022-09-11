package cache;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class ObjectTemplate implements CRUD, Serializable {
    private final int TARGET = 1, PATH = 2, VALUE = 3; // indexes
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

    public void create(String[] commands) {
        if (commands.length < VALUE) {
            System.out.println("Missing arguments");
            return;
        }

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
            default -> {
            }
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

    @Override
    public void read(String[] commands) {

    }

    // todo: change access to private
    public void display() {
        ValueStructure.resetHierarchyLevel();
        for (Map.Entry i : floorMap.entrySet())
            System.out.print("\"" + i.getKey() + "\" : {\n" + i.getValue());
        System.out.println("}");
    }

    public void update(String[] commands) {
        if (commands.length != 4) {
            System.out.println("Missing arguments!");
            return;
        }

        if (commands[TARGET].length() == 0)
            return;

        String target = commands[TARGET];

        if (commands[PATH].length() == 0)
            return;

        var listOfPaths = createListOfPath(commands[PATH]);


        if (commands[VALUE].length() == 0)
            return;

        String value = commands[VALUE].replace("\"", "");

        switch (target) {
            case "key" -> updateKey(listOfPaths, value);
            case "value" -> {
                if (value.equals("OBJECT"))
                    updateValue(listOfPaths, new ValueStructure());
                else
                    updateValue(listOfPaths, new ValueStructure(value));
            }
            default -> System.out.println("Wrong target");

        }
    }

    private void updateKey(LinkedList<String> listOfPaths, String keyToUpdate) {
        recursiveNavigation(listOfPaths);

        String key = listOfPaths.getFirst();
        if (objectDataPointer.containsKey(key)) {
            System.out.println("TODO");
        }
    }

    private void updateValue(LinkedList<String> listOfPaths, ValueStructure valueToUpdate) {
        recursiveNavigation(listOfPaths);

        String key = listOfPaths.getFirst();
        if (objectDataPointer.containsKey(key))
            objectDataPointer.replace(key, valueToUpdate);
        else
            System.out.println("Target doesn't exit");
        objectDataPointer = floorMap;
    }

    public void delete(String path) {
        if (path.length() == 0) return;
        var listOfPaths = createListOfPath(path);

        recursiveNavigation(listOfPaths);
        if (objectDataPointer.remove(listOfPaths.getFirst()) == null)
            System.out.println("Target not found!");

        objectDataPointer = floorMap; // reset pointer;
    }

    private LinkedList<String> createListOfPath(String path) {
        return new LinkedList<>(List.of(path
                .replace("\"", "")
                .split("\\W")));
    }
}