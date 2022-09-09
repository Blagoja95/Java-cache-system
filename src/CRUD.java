import java.util.*;

public class CRUD {
    static LinkedHashMap<String, Object> floorMap = new LinkedHashMap<>();
    static LinkedHashMap objectDataPointer = floorMap;
    private static final int TARGET = 2, PATH = 3, VALUE = 4; // indexes

    private static void recursiveCreation(LinkedList<String> list, int untilIndex) {
        if (list.size() == untilIndex) return;

        String path = list.getFirst();
        ObjectTemplate tempOb;
        if (objectDataPointer.containsKey(path))
            tempOb = (ObjectTemplate) objectDataPointer.get(path);
        else {
            tempOb = new ObjectTemplate();
            objectDataPointer.put(path, tempOb);
        }

        objectDataPointer = tempOb.fields;
        list.removeFirst();
        recursiveCreation(list, untilIndex);
    }

    private static void recursiveNavigation (LinkedList<String> list){
        if (list.size() == 1) return;

        ObjectTemplate tempOb;
        String path = list.getFirst();
        if (objectDataPointer.containsKey(path))
            tempOb = (ObjectTemplate) objectDataPointer.get(path);
        else {
            System.out.println("Target doesn't exist!");
            return;
        }

        objectDataPointer = tempOb.fields;
        list.removeFirst();
        recursiveNavigation(list);

    }

    private static LinkedList<String> createListOfPath(String path) {
        return new LinkedList<>(List.of(path
                .replace("\"", "")
                .split("\\W")));
    }

    static void create(String[] commands) {
        if (commands.length < 4) return;

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

    private static void createObject(LinkedList<String> list) {
        recursiveCreation(list, 0);
        objectDataPointer = floorMap; // reset pointer;
    }

    private static void createField(LinkedList<String> listOfPaths, String value) {
        recursiveCreation(listOfPaths, 1);

        String key = listOfPaths.getFirst();
        if (objectDataPointer.containsKey(key)) {
            System.out.println("An object or a filed with the same name already exist!");
            objectDataPointer = floorMap;
            return;
        }

        objectDataPointer.put(key, value);
        objectDataPointer = floorMap; // reset pointer
    }

    static void display(Map<String, Object> ob) {
        ObjectTemplate.objectHierarchy = 1; // reset
        for (Map.Entry i : ob.entrySet())
            System.out.print("\"" + i.getKey() + "\" : {\n" + i.getValue());
        System.out.println("}");
    }

    static void update(String[] commands) {
        if (commands.length != 5) return;

        if (commands[TARGET].length() == 0) return;
        String target = commands[TARGET];

        if (commands[PATH].length() == 0) return;
        var listOfPaths = createListOfPath(commands[PATH]);


        if (commands[VALUE].length() == 0) return;
        String value = commands[VALUE].replace("\"", "");

        switch (target) {
            case "key" -> updateKey(listOfPaths, value);
            case "value" -> {
                if (value.equals("OBJECT"))
                    updateValue(listOfPaths, new ObjectTemplate());
                else
                    updateValue(listOfPaths, value);
            }
            default -> System.out.println("Wrong target");

        }
    }

    private static void updateKey(LinkedList<String> listOfPaths, String keyToUpdate) {
        recursiveNavigation(listOfPaths);

        String key = listOfPaths.getFirst();
        if (objectDataPointer.containsKey(key)) {
            System.out.println("TODO");
        }
    }

    private static void updateValue(LinkedList<String> listOfPaths, Object valueToUpdate) {
        recursiveNavigation(listOfPaths);

        String key = listOfPaths.getFirst();
        if (objectDataPointer.containsKey(key))
            objectDataPointer.replace(key, valueToUpdate);
        else
            System.out.println("Target doesn't exit");
        objectDataPointer = floorMap;
    }

    static void delete(String path) {
        if (path.length() == 0) return;
        var listOfPaths = createListOfPath(path);

        recursiveNavigation(listOfPaths);
        if (objectDataPointer.remove(listOfPaths.getFirst()) == null)
            System.out.println("Target not found!");

        objectDataPointer = floorMap; // reset pointer;
    }
}

