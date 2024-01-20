package ga.justreddy.wiki.reggwars.model.entity.data.adapters;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

/**
 * @author JustReddy
 */
public abstract class DataAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {}
