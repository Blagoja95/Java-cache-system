import java.util.LinkedHashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;

public class CRUD {
    static LinkedHashMap<String, Object> floorMap = new LinkedHashMap<>();
    static LinkedHashMap objectDataPointer = floorMap;

    private static void recursiveNavigation(LinkedList<String> list, int untilIndex) {
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
        recursiveNavigation(list, untilIndex);
    }

    static void create(String[] commands) {
        final int TARGET = 2, PATH = 3, VALUE = 4;
        if (commands[PATH].length() == 0) {
            System.out.println("Missing arguments!");
            return;
        }

        var listOfPaths = new LinkedList<>(List.of(commands[PATH]
                .replace("\"", "")
                .split("\\W")));

        switch (commands[TARGET]) {
            case "object" -> createObject(listOfPaths);
            case "field" -> {
                if (listOfPaths.size() == 1) {
                    System.out.println("Missing name of the field! Try cache create field \"map.fieldName\" ");
                    return;
                }

                // Index 4 out of bounds for length 4 to do
                if (commands.length != VALUE && commands[VALUE].length() == 0) {
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
            recursiveNavigation(list, 0);
        objectDataPointer = floorMap; // reset pointer;
    }

    private static void createField(LinkedList<String> listOfPaths, String value) {
        recursiveNavigation(listOfPaths, 1);

        String path = listOfPaths.getFirst();
        if (objectDataPointer.containsKey(path)) {
            System.out.println("An object or a filed with the same name already exist!");
            objectDataPointer = floorMap;
            return;
        }

        objectDataPointer.put(path, value);
        objectDataPointer = floorMap; // reset pointer
    }

    static void display(Map<String, Object> ob) {
        ObjectTemplate.objectHierarchy = 1; // reset
        for (Map.Entry i : ob.entrySet())
            System.out.print("\"" + i.getKey() + "\" : {\n" + i.getValue());
        System.out.println("}");
    }

    static void update(String[] commands) {
        final int TARGET = 2, PATH = 3, VALUE = 4;

        if (commands[TARGET].length() == 0) return;
        String target = commands[TARGET];

        if (commands[PATH].length() == 0) return;
        var listOfPaths = new LinkedList<String>(List.of(commands[PATH]
                .replace("\"", "")
                .split("\\W")));

        if (commands[VALUE].length() == 0) return;
        String value = commands[VALUE].replace("\"", "");

        switch (target) {
            case "key" -> updateKey(listOfPaths, value);
//            case "value" -> updateValue(listOfPaths, value);
            default -> {
                System.out.println("Wrong target");
                return;
            }
        }
    }

    private static void updateKey(LinkedList<String> listOfPaths, String valueToUpdate) {
        String path = listOfPaths.getFirst();
        if (objectDataPointer.containsKey(path)) {
            System.out.println("TODO");
        }
    }
}
