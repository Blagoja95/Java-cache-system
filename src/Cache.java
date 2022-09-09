import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Cache {
    static final int MIN_ARGUMENTS = 3;
    static final int MAX_ARGUMENTS = 5;
    static final String HELP_MSG = """
            Available commands:
                
            cache create object "object1.object2"
            cache create field  "object1.field" "value"
            cache read ?
            cache delete ?
            cache update ?""";

    public static void main(String[] args) {
        System.out.println("Welcome to the java cache system");
        System.out.println(HELP_MSG);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                String input = br.readLine();

                if (input.length() == 0){
                    System.out.println("Command can't be empty! Try: cache create object \"planet.name\"");
                    continue;
                }

                if (input.equals("q") || input.equals("quit") || input.equals("exit")) break;

                if (input.equals("help")) {
                    System.out.println(HELP_MSG);
                    continue;
                }

                String[] commands = input.split("\\s+");

                if (commands.length >= MIN_ARGUMENTS && commands.length <= MAX_ARGUMENTS && commands[0].equals("cache"))
                    switch (commands[1]) {
                        case "create" -> CRUD.create(commands);
                        case "read" -> {
                            System.out.println("read todo");
                        }
                        case "delete" -> CRUD.delete(commands[2]);
                        case "update" -> CRUD.update(commands);
                        default -> System.out.println("Wrong command. Try: cache create object \"object1\"");
                    }
                else System.out.println("Wrong command. Try command help");
                // to do dev: remove later
                CRUD.display(CRUD.floorMap);
            }
        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }
}