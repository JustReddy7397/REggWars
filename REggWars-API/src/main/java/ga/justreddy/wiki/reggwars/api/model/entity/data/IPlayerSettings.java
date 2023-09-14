package ga.justreddy.wiki.reggwars.api.model.entity.data;

import ga.justreddy.wiki.reggwars.api.model.language.ILanguage;

/**
 * @author JustReddy
 */
public interface IPlayerSettings {

    ILanguage getLanguage();

    void setLanguage(ILanguage language);

}
