import java.util.LinkedHashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;

public class CRUD {
    static LinkedHashMap<String, Object> floorMap = new LinkedHashMap<>();
    static LinkedHashMap objectDataPointer = floorMap;

    static void create(String[] commands) {
        if (commands[3].length() == 0) {
            System.out.println("Missing arguments!");
            return;
        }

        var listOfPaths = new LinkedList<>(List.of(commands[3].replace("\"", "").split("\\W")));

        switch (commands[2]) {
            case "object":
                createObject(listOfPaths);
                break;

            case "field":
                if (listOfPaths.size() == 1) {
                    System.out.println("Missing name of the field! Try cache create field \"map.fieldName\" ");
                    return;
                }

                if (commands.length != 4 && commands[4].length() == 0) {
                    System.out.println("Missing filed value!");
                    return;
                }

                String value = commands[4];
                createField(listOfPaths, value);
                break;

            default:
                System.out.println("Missing commands. Try using object or field after create : cache create field \"object.field\" \"value\"");
                break;
        }
    }

    static void createObject(LinkedList<String> list) {
        ObjectTemplate tempOb;
        String path = list.getFirst();

        if (objectDataPointer.containsKey(path)) tempOb = (ObjectTemplate) objectDataPointer.get(path);
        else {
            tempOb = new ObjectTemplate();
            objectDataPointer.put(path, tempOb);
        }

        objectDataPointer = tempOb.fields;
        list.removeFirst();

        if (list.size() != 0) createObject(list);
        objectDataPointer = floorMap; // reset pointer;
    }

    static void createField(LinkedList<String> listOfPaths, String value) {
        ObjectTemplate tempOb;
        String path = listOfPaths.getFirst();

        if (listOfPaths.size() == 1) {
            if (objectDataPointer.containsKey(path)) {
                System.out.println("An object or a filed with the same name already exist!");
                objectDataPointer = floorMap;
                return;
            }

            objectDataPointer.put(path, value);
            objectDataPointer = floorMap;
            return;
        }

        if (objectDataPointer.containsKey(path))
            tempOb = (ObjectTemplate) objectDataPointer.get(path);
        else {
            tempOb = new ObjectTemplate();
            objectDataPointer.put(path, tempOb);
        }

        objectDataPointer = tempOb.fields;
        listOfPaths.removeFirst();
        createField(listOfPaths, value);
    }

    static void display(Map<String, Object> ob) {
        ObjectTemplate.objectHierarchy = 1; // reset
        for (Map.Entry i : ob.entrySet())
            System.out.print("\"" + i.getKey() + "\" : {\n" + i.getValue());
        System.out.println("}");
    }

}
