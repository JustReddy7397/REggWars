package ga.justreddy.wiki.reggwars.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JustReddy
 */
public class ConfigWriter {

    private File file;
    private final Map<String, Object> map = new LinkedHashMap<>();

    public ConfigWriter(File file) {
        this.file = file;
    }

    public void write() {
        try {
            Writer fw = new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8);
            fw.append(toSaveString());
            fw.close();
        } catch (IOException ex) {
            ex.fillInStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void set(String path, Object value) {
        String[] splitter = path.split("\\.");

        Map<String, Object> currentMap = this.map;
        for (int slot = 0; slot < splitter.length; slot++) {
            String p = splitter[slot];
            if (slot + 1 == splitter.length) {
                currentMap.put(p, value);
                continue;
            } else {
                if (!currentMap.containsKey(p)) {
                    currentMap.put(p, new LinkedHashMap<String, Object>());
                }
                currentMap = (Map<String, Object>) currentMap.get(p);
            }
        }
    }

    public String toSaveString() {
        StringBuilder join = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            join.append(toSaveString(entry.getKey(), entry.getValue(), 0));
        }

        return join.toString();
    }

    @SuppressWarnings("unchecked")
    private String toSaveString(String key, Object object, int spaces) {
        StringBuilder join = new StringBuilder(repeat(spaces) + key + ":");
        if (object instanceof String) {
            join.append(" '").append(object.toString().replace("'", "''").replace("\"", "\"\"")).append("'\n");
        } else if (object instanceof Integer) {
            join.append(" ").append(object).append("\n");
        } else if (object instanceof Double) {
            join.append(" ").append(object).append("\n");
        } else if (object instanceof Long) {
            join.append(" ").append(object).append("\n");
        } else if (object instanceof Boolean) {
            join.append(" ").append(object).append("\n");
        } else if (object instanceof List) {
            join.append("\n");
            for (Object obj : (List<?>) object) {
                if (obj instanceof Integer) {
                    join.append(repeat(spaces)).append("- ").append(obj.toString()).append("\n");
                } else {
                    join.append(repeat(spaces)).append("- '").append(obj.toString()).append("'\n");
                }
            }
        } else if (object instanceof Map) {
            join.append("\n");
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) object).entrySet()) {
                join.append(toSaveString(entry.getKey(), entry.getValue(), spaces + 1));
            }
        }

        return join.toString();
    }

    private String repeat(int spaces) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            sb.append(" ");
        }

        return sb.toString();
    }

}
