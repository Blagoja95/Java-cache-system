import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Cache {
    static LinkedHashMap<String, Object> floorMap = new LinkedHashMap<>();
    static LinkedHashMap objectDataPointer = floorMap;

    static final String HELP_MSG = """
            Available commands:
                
            cache create object/field "object1.object2.filed1"
            cache read ?
            cache delete ?
            cache update ?""";

    static void read(Map<String, Object> ob) {
        for (Map.Entry i : ob.entrySet())
            System.out.print("\"" + i.getKey() + "\" : {\n" + i.getValue());
        System.out.println("}");
    }

    static void create(String[] commands) {
        switch (commands[2]) {
            case "object":
                var createPath = new LinkedList<>
                        (List.of(commands[3].replace("\"", "").split("\\W")));

                if (createPath.size() == 0) return;
                createObject(createPath);
                break;

            case "field":
                System.out.println("Creating field");
                break;

            default:
                System.out.println("Missing commands");
                break;
        }
    }

    static void createObject(LinkedList<String> list) {
        ObjectTemplate tempOb;
        String path = list.getFirst();

        if (objectDataPointer.containsKey(path))
            tempOb = (ObjectTemplate) objectDataPointer.get(path);
        else {
            tempOb = new ObjectTemplate();
            objectDataPointer.put(list.getFirst(), tempOb);
        }

        objectDataPointer = tempOb.fields;
        list.removeFirst();

        if (list.size() != 0) createObject(list);
        objectDataPointer = floorMap; // reset pointer;
    }

    public static void main(String[] args) {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                String input = br.readLine();
                if (input.length() == 0)
                    System.out.println("Command can't be empty! Try: cache create object \"planet.name\"");

                if (input.equals("q") || input.equals("quit")
                        || input.equals("exit")) break;

                if (input.equals("help")) {
                    System.out.println(HELP_MSG);
                    continue;
                }

                String[] commands = input.split("\\s+");

                if (commands.length >= 4 && commands[0].equals("cache"))
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
                else
                    System.out.println("Wrong command. Try command help");
            }
        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }
}


