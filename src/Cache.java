import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Cache {
    static LinkedHashMap<String, Object> floorMap = new LinkedHashMap<>();
    static LinkedHashMap objectDataPointer = floorMap;
    static final int MIN_ARGUMENTS = 3;
    static final int MAX_ARGUMENTS = 5;
    static final String HELP_MSG = """
            Available commands:
                
            cache create object "object1.object2"
            cache create field  "object1.field" "value"
            cache read ?
            cache delete ?
            cache update ?""";

    static void read(Map<String, Object> ob) {
        for (Map.Entry i : ob.entrySet())
            System.out.print("\"" + i.getKey() + "\" : {\n" + i.getValue());
        System.out.println("}");
    }

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

        if (objectDataPointer.containsKey(path)) tempOb = (ObjectTemplate) objectDataPointer.get(path);
        else {
            tempOb = new ObjectTemplate();
            objectDataPointer.put(path, tempOb);
        }

        objectDataPointer = tempOb.fields;
        listOfPaths.removeFirst();
        createField(listOfPaths, value);
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the java cache system");
        System.out.println(HELP_MSG);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                String input = br.readLine();

                if (input.length() == 0)
                    System.out.println("Command can't be empty! Try: cache create object \"planet.name\"");

                if (input.equals("q") || input.equals("quit") || input.equals("exit")) break;

                if (input.equals("help")) {
                    System.out.println(HELP_MSG);
                    continue;
                }

                String[] commands = input.split("\\s+");

                if (commands.length > MIN_ARGUMENTS && commands.length <= MAX_ARGUMENTS && commands[0].equals("cache"))
                    switch (commands[1]) {
                        case "create":
                            create(commands);
                            read(floorMap);
                            break;

                        case "read":
                            System.out.println("read todo");
                            break;

                        case "delete":
                            System.out.println("delete todo");
                            break;

                        case "update":
                            System.out.println("update todo");
                            break;

                        default:
                            System.out.println("Wrong command. Try: cache create object \"object1\"");
                            break;
                    }
                else System.out.println("Wrong command. Try command help");
            }
        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }
}