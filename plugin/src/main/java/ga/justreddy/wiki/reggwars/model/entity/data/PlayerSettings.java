package ga.justreddy.wiki.reggwars.model.entity.data;

import ga.justreddy.wiki.reggwars.api.model.entity.data.IPlayerSettings;
import ga.justreddy.wiki.reggwars.api.model.language.ILanguage;
import ga.justreddy.wiki.reggwars.manager.LanguageManager;

/**
 * @author JustReddy
 */
public class PlayerSettings implements IPlayerSettings {

    private ILanguage language;

    @Override
    public ILanguage getLanguage() {
        return language == null ? language = LanguageManager.getManager().getLanguage("en") : language;
    }

    @Override
    public void setLanguage(ILanguage language) {
        this.language = language;
    }
}
