package ga.justreddy.wiki.reggwars.velocity.config.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.*;

/**
 * @author JustReddy
 */

@FieldDefaults(level = AccessLevel.PROTECTED)
public class YamlConfigurationSection implements ConfigurationSection {

    /**
     * Represents the base configuration section
     */
    final ConfigurationSection baseSection;

    /**
     * Represents this configuration section
     */
    Map<String, Object> data;

    /**
     * Represents the location of the section in the base section
     */
    final String rootPath;

    /**
     * Used to create a new base configuration section
     *
     * @param base The data located in the base configuration section
     */
    public YamlConfigurationSection(Map<String, Object> base) {
        this.data = base;
        this.baseSection = this;
        this.rootPath = null;
    }

    /**
     * Used to create a new configuration section that represents
     * a subsection of the config file
     *
     * @param base The data of the base configuration section
     * @param path The location of the subsection in the base section
     */
    public YamlConfigurationSection(Map<String, Object> base, String path) {
        this.baseSection = new YamlConfigurationSection(base);
        this.rootPath = path;

        this.data = base;
        this.data = getSection(path).getData();
    }

    /**
     * Used to create a new configuration section that represents
     * a subsection of the config file
     *
     * @param base The data of the base configuration section
     * @param path The location of the subsection in the base section
     * @param data The data located in the subsection
     */
    public YamlConfigurationSection(Map<String, Object> base, String path, Map<String, Object> data) {
        this.baseSection = new YamlConfigurationSection(base);
        this.rootPath = path;
        this.data = data;
    }


    @Override
    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public String getBasePath() {
        return rootPath;
    }

    @Override
    public String getBasePath(String path) {
        if (this.rootPath == null) return path;
        if (path == null) return rootPath;

        return rootPath + "." + path;
    }

    @Override
    public void set(Object value) {
        baseSection.setInSection(getBasePath(), value);
    }

    @Override
    public void set(String path, Object value) {
        baseSection.setInSection(getBasePath(path), value);
    }

    @Override
    public void setInSection(String path, Object value) {
        // If the value is located in a subsection
        if (path != null && path.contains(".")) {
            // Get the child path
            String key = path.split("\\.")[0];
            // Get the remaining children
            String remainingPath = path.substring(key.length() + 1);

            // Create the section if it doesn't exist
            if (getSection(key) == null) {
                data.put(key, new HashMap<String, Object>());
            }

            // Get the subsection
            YamlConfigurationSection section = new YamlConfigurationSection(
                    baseSection.getData(),
                    getBasePath(key),
                    getSection(key).getData()
            );

            // Set the value in the section
            section.setInSection(remainingPath, value);

            // Get the updated section and update this section to the new data
            data.put(key, section.getData());
            return;
        }

        // If the value is being set to null
        if (value == null) {

            // If the path location is in this section
            if (path == null) {
                data = new HashMap<>();
                return;
            }

            this.data.remove(path);
            return;
        }

        data.put(path, value);
    }

    @Override
    public Object get(String path, Object alternative) {
        if (path.contains(".")) {
            // The child path
            String key = path.split("\\.")[0];
            // The remaining children
            String remainingPath = path.substring(key.length() + 1);

            // Temporally create the section if it doesn't exist
            if (this.getSection(key) == null) {
                this.data.put(key, new HashMap<String, Object>());
            }

            return getSection(key).get(remainingPath);
        }

        return data.getOrDefault(path, alternative);
    }

    @Override
    public Object get(String path) {
        return get(path, null);
    }

    @Override
    public ConfigurationSection getSection(String path) {
        if (path == null) return this;

        // Return a new empty section if it does not exist
        if (!(this.get(path) instanceof Map<?, ?>)) {
            return new YamlConfigurationSection(this.baseSection.getData(), this.getBasePath(path), new HashMap<>());
        }

        Map<?,?> unknownMap = (Map<?, ?>) get(path);

        // Get the section and return it
        Map<String, Object> knownMap = new LinkedHashMap<>();

        for (Map.Entry<?, ?> entry : unknownMap.entrySet()) {
            knownMap.put(entry.getKey().toString(), entry.getValue());
        }

        return new YamlConfigurationSection(this.baseSection.getData(), this.getBasePath(path), knownMap);
    }

    @Override
    public List<String> getKeys() {
        if (this.data == null) return null;
        return new ArrayList<>(data.keySet());
    }

    @Override
    public List<String> getKeys(String path) {
        return getSection(path).getKeys();
    }

    @Override
    public List<?> getList(String path, List<?> alternative) {
        Object object = get(path);
        return object instanceof List<?> ? (List<?>) object : alternative;
    }

    @Override
    public List<?> getList(String path) {
        return getList(path, null);
    }

    @Override
    public List<String> getStringList(String path, List<String> alternative) {
        List<String> list = new ArrayList<>();

        for (Object item : getList(path, new ArrayList<>())) {
            if (!(item instanceof String)) return alternative;
            list.add((String) item);
        }

        return list;
    }

    @Override
    public List<String> getStringList(String path) {
        return getStringList(path, null);
    }

    @Override
    public List<Integer> getIntegerList(String path, List<Integer> alternative) {
        List<Integer> list = new ArrayList<>();

        for (Object item : getList(path, new ArrayList<>())) {
            if (!(item instanceof Integer)) return alternative;
            list.add((Integer) item);
        }

        return list;
    }

    @Override
    public List<Integer> getIntegerList(String path) {
        return getIntegerList(path, null);
    }

    @Override
    public String getString(String path, String alternative) {
        Object object = get(path);
        return object instanceof String ? (String) object : alternative;
    }

    @Override
    public String getString(String path) {
        return getString(path, null);
    }

    @Override
    public int getInteger(String path, int alternative) {
        Object object = get(path);
        return object instanceof Integer ? (Integer) object : alternative;
    }

    @Override
    public int getInteger(String path) {
        return getInteger(path, 0);
    }

    @Override
    public boolean getBoolean(String path, boolean alternative) {
        Object object = get(path);
        return object instanceof Boolean ? (Boolean) object : alternative;
    }

    @Override
    public boolean getBoolean(String path) {
        return getBoolean(path, false);
    }

}
