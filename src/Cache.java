import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.LinkedHashMap;

public class Cache {

    static void printJson(Map<String, Object> ob) {
        for (Map.Entry i : ob.entrySet())
            System.out.println("\"" + i.getKey() + "\" : \n" + i.getValue());
        System.out.println("}");
    }

    public static void main(String[] args) {
        var map = new LinkedHashMap<String, Object>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            while ( true ){
                String input = br.readLine();

                if (input.equals("q") || input.equals("quit")
                        || input.equals("exit")) break;

                if (input.length() == 0)
                    System.out.println("Command can't be empty! Try: cache create object \"planet.name\"");

                String[] commands= input.split("\\s");

                if (commands[0].equals("cache"))
                    for (int i = 1; i < commands.length; i++)
                        System.out.println(commands[i]);
                else
                    System.out.println("Did you mean cache. Try: cache read");
            }
        }catch (IOException ex){
            System.out.println(ex);
            ex.printStackTrace();
        }

        // to do
//        printJson(map);
//        ObjectTemplate ob = (ObjectTemplate) map.get("Planets");

//        System.out.println( "Numbers : {\n" +
//        ob.get("Numbers") + "}");
    }
}


