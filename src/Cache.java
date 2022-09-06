import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.util.*;

public class Cache {

    static void printJson(Map<String, Object> ob) {
        for (Map.Entry i : ob.entrySet())
            if (i.getValue() != null)
                System.out.println(i.toString());
            else
                System.out.println(i.getKey() + " = " + "{ }");
    }

    public static void main(String[] args) {
        var map = new LinkedHashMap<String, Object>();
        ObjectTemplate a = new ObjectTemplate();

        a.fields.put("New", "Map");
//        a.fields.put("Array", new int[]{1, 2 ,4 , 5});
        a.fields.put("Boolean", true);
        a.fields.put("Bool", false);

        ObjectTemplate b = new ObjectTemplate();
        b.fields.put("Integer", 123);
        b.fields.put("Double", 123.34);

        a.fields.put("Numbers", b);

        a.fields.put("NotNumber", null);

        ObjectTemplate as = new ObjectTemplate();
        ObjectTemplate car = new ObjectTemplate();

        car.fields.put("Engine", "inline");
        car.fields.put("Doors", 2);
        car.fields.put("Wheels", 4);
        car.fields.put("Color", "Red");

        as.put("Car parts", car);

        a.fields.put("Car", as);

        map.put("Planets", a);

        for (Map.Entry<String, Object> i : map.entrySet())
            System.out.println("\"" + i.getKey() + "\" : \n" + i.getValue());


    }
}

class ObjectTemplate {
    static int lvl = 1;
    static boolean first = false;
    LinkedHashMap<String, Object> fields = new LinkedHashMap<>();

    ObjectTemplate() {
    }

    ;

    public void put(String key, Object ob) {
        fields.put(key, ob);
    }

    public Object get(String key) {
        return fields.get(key);
    }

    @Override
    public String toString() {
        if (fields.size() == 0) return "{}";

        StringBuilder string = new StringBuilder("");

        if (!first)
            for (int i = lvl; i > 0; i--)
                string.append("\t");


//        Iterator<Map.Entry<String, Object>> entry = fields.entrySet().iterator();


        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            string.append("\"").append(entry.getKey()).append("\" : ");


            if (entry.getValue() == null) {
                string.append(" {},\n");

            } else if (entry.getValue() instanceof ObjectTemplate) {
                ObjectTemplate temp = (ObjectTemplate) entry.getValue();
                first = false;
                lvl++;
                string.append("{\n").append(temp.toString()).append("\n}\n");


            } else{
                string.append("\"").append(entry.getValue()).append("\",\n");

            for (int i = lvl; i > 0; i--)
                string.append("\t");

            first = true;
            }
        }


        return string.toString();
    }
}