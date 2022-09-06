import java.util.Map;
import java.util.LinkedHashMap;

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

        // creating and fixing objects
        ObjectTemplate a = new ObjectTemplate();
        a.put("New", "Map");
//        a.fields.put("Array", new int[]{1, 2 ,4 , 5});
        a.put("Boolean", true);
        a.put("Bool", false);

        ObjectTemplate b = new ObjectTemplate();
        b.put("Integer", 123);
        b.put("Double", 123.34);

        a.put("Numbers", b);

        a.put("NotNumber", null);

        ObjectTemplate as = new ObjectTemplate();
        ObjectTemplate car = new ObjectTemplate();

        car.put("Engine", "inline");
        car.put("Doors", 2);
        car.put("Wheels", 4);
        car.put("Color", "Red");

        as.put("Car parts", car);


        a.put("Car", as);

        map.put("Planets", a);

        // to do
        for (Map.Entry<String, Object> i : map.entrySet())
            System.out.println("\"" + i.getKey() + "\" : \n" + i.getValue());
        System.out.println("}");

        ObjectTemplate ob = (ObjectTemplate) map.get("Planets");

        System.out.println( "Numbers : {\n" +
        ob.get("Numbers") + "}");
    }
}

