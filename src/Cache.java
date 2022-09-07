import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Cache {

    static void read(Map<String, Object> ob) {
        for (Map.Entry i : ob.entrySet())
            System.out.println("\"" + i.getKey() + "\" : \n" + i.getValue());
        System.out.println("}");
    }

    static void create(String[] commands) {
        switch (commands[2]) {
            case "object":
                var createPath = new LinkedList<>
                        (List.of(commands[3].replace("\"", "").split("\\W")));


                break;

            case "filed":
                System.out.println("Creating filed");
                break;

            default:

        }
    }

    static ObjectTemplate createObject() {
        return new ObjectTemplate();
    }


    public static void main(String[] args) {
        var floorMap = new LinkedHashMap<String, Object>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while ( true ) {
                String input = br.readLine();

                if (input.equals("q") || input.equals("quit")
                        || input.equals("exit")) break;

                if (input.length() == 0)
                    System.out.println("Command can't be empty! Try: cache create object \"planet.name\"");

                String[] commands = input.split("\\s");

                if (commands.length >= 4  && commands[0].equals("cache"))
                    switch (commands[1]) {
                        case "create":
                            create(commands);
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


