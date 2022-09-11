package cache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Cache {
    static final int MIN_ARGUMENTS = 3;
    static final int MAX_ARGUMENTS = 5;
    static final String HELP_MSG = """
            Available commands:
                
            1. cache create object "object1.object2"
               cache create field  "object1.field" "value"
            2. cache read ?
            3. cache delete "object1.object2.deleteTarget"
            4. cache update value "object1.updateTarget" "updateWith"
               cache update value "object1.updateTarget" "OBJECT" // to update it with a new object
               cache update key ? TODO""";

    public static void main(String[] args) {
        System.out.println("Welcome to the java cache system \n");
        System.out.println(HELP_MSG);

        ObjectTemplate cache = new ObjectTemplate();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                String input = br.readLine();

                if (input.length() == 0){
                    System.out.println("Command can't be empty! Try: cache create object \"planet.name\"");
                    continue;
                }

                if (input.equals("q") || input.equals("quit") || input.equals("exit"))
                    break;

                if (input.equals("help")) {
                    System.out.println(HELP_MSG);
                    continue;
                }

                String[] commands = input.split("\\s+");

                if (commands.length >= MIN_ARGUMENTS && commands.length <= MAX_ARGUMENTS && commands[0].equals("cache"))
                    switch (commands[1]) {
                        case "create" -> cache.create(commands);
                        case "read" -> {
                            System.out.println("read todo");
                        }
//                        case "delete" -> cache.delete(commands[2]);
//                        case "update" -> cache.update(commands);
                        default -> System.out.println("Wrong command. Try: cache create object \"object1\"");
                    }
                else System.out.println("Wrong command. Try command help");
                // to do dev: remove later
                cache.display();
            }
        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }
}