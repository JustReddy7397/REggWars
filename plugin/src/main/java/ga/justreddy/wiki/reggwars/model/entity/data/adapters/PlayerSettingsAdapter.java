package ga.justreddy.wiki.reggwars.model.entity.data.adapters;

import com.google.gson.*;
import ga.justreddy.wiki.reggwars.api.model.language.ILanguage;
import ga.justreddy.wiki.reggwars.manager.LanguageManager;
import ga.justreddy.wiki.reggwars.model.entity.data.PlayerSettings;

import java.lang.reflect.Type;

/**
 * @author JustReddy
 */
public class PlayerSettingsAdapter extends DataAdapter<PlayerSettings> {

    @Override
    public PlayerSettings deserialize(JsonElement jsonElement, Type type,
                                      JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        ILanguage language = LanguageManager.getManager().getLanguage(object.get("language").getAsString());
        if (language == null) language = LanguageManager.getManager().getLanguage("en");
        PlayerSettings settings = new PlayerSettings();
        settings.setLanguage(language);
        return settings;
    }

    @Override
    public JsonElement serialize(PlayerSettings settings, Type type,
                                 JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("language", settings.getLanguage().getId());
        return object;
    }
}
