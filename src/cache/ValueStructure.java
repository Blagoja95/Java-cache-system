package cache;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class ValueStructure implements Serializable {
    private static int objectHierarchy = 1;
    private String stringValue;
    public LinkedHashMap<String, ValueStructure> valueMap = new LinkedHashMap<>();

    ValueStructure() {
    }

    ValueStructure(String value) {
        this.stringValue = value;
    }

    public static void resetHierarchyLevel() {
        objectHierarchy = 1;
    }

    @Override
    public String toString() {
        if (valueMap.size() == 0)
            return "";

        StringBuilder string = new StringBuilder();
        int tabsCount = objectHierarchy++;

        for (Map.Entry<String, ValueStructure> entry : valueMap.entrySet()) {
            string.append(appendTabs(tabsCount)); // first
            string.append("\"")
                    .append(entry.getKey())
                    .append("\" : ");

            ValueStructure temp = entry.getValue();

            if (temp == null) {
                string.append(" {},\n");
            } else if (temp.stringValue != null) {
                string.append(" ")
                        .append(temp.stringValue);
                string.append(",\n");
            } else {
                string.append("{\n")
                        .append(temp);
                string.append(appendTabs(tabsCount));
                string.append("},\n");
                objectHierarchy--;
            }
        }
        return string.toString();
    }

    private StringBuilder appendTabs(int tabsCount) {
        StringBuilder string = new StringBuilder();
        string.append("\t".repeat(Math.max(0, tabsCount)));
        return string;
    }
}
