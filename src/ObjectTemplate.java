import java.util.LinkedHashMap;
import java.util.Map;

class ObjectTemplate {
    static int objectHierarchy = 1;
    public LinkedHashMap<String, Object> fields = new LinkedHashMap<>();

    public void put(String key, Object ob) { fields.put(key, ob); }
    public Object get(String key) {
        return fields.get(key);
    }

    @Override
    public String toString() {
        if (fields.size() == 0) return "";

        StringBuilder string = new StringBuilder("");
        int tabsCount = objectHierarchy++;

        for (Map.Entry<String, Object> entry : fields.entrySet()) {

            string.append(appendTabs(tabsCount)); // first
            string.append("\"").append(entry.getKey()).append("\" : ");

            if (entry.getValue() == null) {
                string.append(" {},\n");

            } else if (entry.getValue() instanceof ObjectTemplate) {
                ObjectTemplate temp = (ObjectTemplate) entry.getValue();

                string.append("{\n").append(temp);
                string.append(appendTabs(tabsCount));
                string.append("},\n");
                objectHierarchy--;

            } else string.append("\"").append(entry.getValue()).append("\",\n");
        }
        return string.toString();
    }

    private StringBuilder appendTabs(int tabsCount) {
        StringBuilder string = new StringBuilder();

        for (int i = tabsCount; i > 0; i--)
            string.append("\t");

        return string;
    }
}