import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Cache {
    static LinkedHashMap floorMap = new LinkedHashMap<String, Object>();
    static LinkedHashMap objectDataPointer;

    static void read(Map<String, Object> ob) {
        for (Map.Entry i : ob.entrySet())
            System.out.println("\"" + i.getKey() + "\" : {\n" + i.getValue());
        System.out.println("}");
    }

    static void create(String[] commands) {
        switch (commands[2]) {
            case "object":
                var createPath = new LinkedList<>
                        (List.of(commands[3].replace("\"", "").split("\\W")));

                // create first
                String tempPath = createPath.getFirst();
                if (!floorMap.containsKey(tempPath)){
                    ObjectTemplate tempObject = new ObjectTemplate();
                    floorMap.put(tempPath, tempObject);
                    createPath.removeFirst();
                    objectDataPointer = tempObject.fields;
                }q

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

    static void createObject(LinkedList list) {

//        if (objectDataPointer.containsKey(list.getFirst()))

        ObjectTemplate tempOb = new ObjectTemplate();
        objectDataPointer.put(list.getFirst(), tempOb);
        objectDataPointer = tempOb.fields;
        list.removeFirst();

        if (list.size() != 0) createObject(list);

        objectDataPointer = floorMap; // reset pointer;
    }


    public static void main(String[] args) {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                String input = br.readLine();

                if (input.equals("q") || input.equals("quit")
                        || input.equals("exit")) break;

                if (input.length() == 0)
                    System.out.println("Command can't be empty! Try: cache create object \"planet.name\"");

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
                            System.out.println("Wrong command. Try: cache create \"object1\"");
                            break;
                    }
                else
                    System.out.println("Wrong command! Try: cache create \"objectName\"");
            }
        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
//         to do
//        printJson(map);
//        ObjectTemplate ob = (ObjectTemplate) map.get("Planets");
//
//        System.out.println( "Numbers : {\n" +
//        ob.get("Numbers") + "}");
    }
}


