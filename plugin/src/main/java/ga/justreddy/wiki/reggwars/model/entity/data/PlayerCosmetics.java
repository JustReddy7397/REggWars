package ga.justreddy.wiki.reggwars.model.entity.data;

import eu.decentsoftware.holograms.api.utils.config.FileConfig;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.data.IPlayerCosmetics;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.checkerframework.checker.units.qual.A;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author JustReddy
 */
public class PlayerCosmetics implements IPlayerCosmetics, ConfigurationSerializable {

    // TODO Check if cosmetic ids exist

    private int selectedCage;
    private Set<Integer> cages;

    private int selectedDance;
    private Set<Integer> dances;

    private int selectedKillEffect;
    private Set<Integer> killEffects;

    private int selectedTrail;
    private Set<Integer> trails;

    private int selectedKillMessage;
    private Set<Integer> killMessages;

    private int selectedDeathCry;
    private Set<Integer> deathCries;

    public PlayerCosmetics() {
        FileConfiguration config = REggWars.getInstance().getSettingsConfig()
                .getConfig();
        this.selectedCage = config.getInt("player.cosmetics.default-cage");
        this.selectedDance = config.getInt("player.cosmetics.default-dance");
        this.selectedKillEffect = config.getInt("player.cosmetics.default-kill-effect");
        this.selectedTrail = config.getInt("player.cosmetics.default-trail");
        this.selectedKillMessage = config.getInt("player.cosmetics.default-kill-message");
        this.selectedDeathCry = config.getInt("player.cosmetics.default-death-cry");

        this.cages = new HashSet<>();
        this.dances = new HashSet<>();
        this.killEffects = new HashSet<>();
        this.trails = new HashSet<>();
        this.killMessages = new HashSet<>();
        this.deathCries = new HashSet<>();

        this.cages.add(selectedCage);
        this.dances.add(selectedDance);
        this.killEffects.add(selectedKillEffect);
        this.trails.add(selectedTrail);
        this.killMessages.add(selectedKillMessage);
        this.deathCries.add(selectedDeathCry);

        config = null; // Can be garbage collected since we dont need it anymore after
    }

    @Override
    public int getSelectedCage() {
        return selectedCage;
    }

    @Override
    public void setSelectedCage(int id) {
        if (!hasCage(id)) return; // Don't want to set cages they don't have right :)
        this.selectedCage = id;
    }

    @Override
    public Set<Integer> getCages() {
        return cages;
    }

    @Override
    public void addCage(int id) {
        this.cages.add(id);
    }

    @Override
    public boolean hasCage(int id) {
        return cages.contains(id);
    }

    @Override
    public void removeCage(int id) {
        this.cages.remove(id);
    }

    @Override
    public int getSelectedDance() {
        return selectedDance;
    }

    @Override
    public void setSelectedDance(int id) {
        if (!hasDance(id)) return; // Don't want to set dances they don't have right :)
        this.selectedDance = id;
    }

    @Override
    public Set<Integer> getDances() {
        return dances;
    }

    @Override
    public void addDance(int id) {
        this.dances.add(id);
    }

    @Override
    public boolean hasDance(int id) {
        return dances.contains(id);
    }

    @Override
    public void removeDance(int id) {
        this.dances.remove(id);
    }

    @Override
    public int getSelectedKillEffect() {
        return selectedKillEffect;
    }

    @Override
    public void setSelectedKillEffect(int id) {
        if (!hasKillEffect(id)) return; // Don't want to set kill effects they don't have right :)
        this.selectedKillEffect = id;
    }

    @Override
    public Set<Integer> getKillEffects() {
        return killEffects;
    }

    @Override
    public void addKillEffect(int id) {
        this.killEffects.add(id);
    }

    @Override
    public boolean hasKillEffect(int id) {
        return killEffects.contains(id);
    }

    @Override
    public void removeKillEffect(int id) {
        this.killEffects.remove(id);
    }

    @Override
    public int getSelectedKillMessage() {
        return selectedKillMessage;
    }

    @Override
    public void setSelectedKillMessage(int id) {
        if (!hasKillMessage(id)) return; // Don't want to set kill messages they don't have right :)
        this.selectedKillMessage = id;
    }

    @Override
    public Set<Integer> getKillMessages() {
        return killMessages;
    }

    @Override
    public void addKillMessage(int id) {
        this.killMessages.add(id);
    }

    @Override
    public boolean hasKillMessage(int id) {
        return killMessages.contains(id);
    }

    @Override
    public void removeKillMessage(int id) {
        this.killMessages.remove(id);
    }

    @Override
    public int getSelectedTrail() {
        return selectedTrail;
    }

    @Override
    public void setSelectedTrail(int id) {
        if (!hasTrail(id)) return; // Don't want to set trails they don't have right :)
        this.selectedTrail = id;
    }

    @Override
    public Set<Integer> getTrails() {
        return trails;
    }

    @Override
    public void addTrail(int id) {
        this.trails.add(id);
    }

    @Override
    public boolean hasTrail(int id) {
        return trails.contains(id);
    }

    @Override
    public void removeTrail(int id) {
        this.trails.remove(id);
    }

    @Override
    public int getSelectedDeathCry() {
        return selectedDeathCry;
    }

    @Override
    public void setSelectedDeathCry(int id) {
        if (!hasDeathCry(id)) return; // Don't want to set death cries they don't have right :)
        this.selectedDeathCry = id;
    }

    @Override
    public void addDeathCry(int id) {
        this.deathCries.add(id);
    }

    @Override
    public boolean hasDeathCry(int id) {
        return deathCries.contains(id);
    }

    @Override
    public void removeDeathCry(int id) {
        this.deathCries.remove(id);
    }

    @Override
    public Set<Integer> getDeathCries() {
        return deathCries;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();

        data.put("selectedCage", selectedCage);
        data.put("cages", cages.toArray());

        data.put("selectedDance", selectedDance);
        data.put("dances", dances.toArray());

        data.put("selectedKillEffect", selectedKillEffect);
        data.put("killEffects", killEffects.toArray());

        data.put("selectedTrail", selectedTrail);
        data.put("trails", trails.toArray());

        data.put("selectedKillMessage", selectedKillMessage);
        data.put("killMessages", killMessages.toArray());

        data.put("selectedDeathCry", selectedDeathCry);
        data.put("deathCries", deathCries.toArray());
        return data;
    }

    public static PlayerCosmetics deserialize(Map<String, Object> data) {
        int selectedCage = (int) data.get("selectedCage");
        int selectedDance = (int) data.get("selectedDance");
        int selectedKillEffect = (int) data.get("selectedKillEffect");
        int selectedTrail = (int) data.get("selectedTrail");
        int selectedKillMessage = (int) data.get("selectedKillMessage");
        int selectedDeathCry = (int) data.get("selectedDeathCry");

        Set<Integer> cages = (HashSet<Integer>) data.get("cages");
        Set<Integer> dances = (HashSet<Integer>) data.get("dances");
        Set<Integer> killEffects = (HashSet<Integer>) data.get("killEffects");
        Set<Integer> trails = (HashSet<Integer>) data.get("trails");
        Set<Integer> killMessages = (HashSet<Integer>) data.get("killMessages");
        Set<Integer> deathCries = (HashSet<Integer>) data.get("deathCries");

        PlayerCosmetics cosmetics = new PlayerCosmetics();
        cosmetics.cages = cages;
        cosmetics.dances = dances;
        cosmetics.killEffects = killEffects;
        cosmetics.trails = trails;
        cosmetics.killMessages = killMessages;
        cosmetics.deathCries = deathCries;
        cosmetics.selectedCage = selectedCage;
        cosmetics.selectedDance = selectedDance;
        cosmetics.selectedKillEffect = selectedKillEffect;
        cosmetics.selectedTrail = selectedTrail;
        cosmetics.selectedKillMessage = selectedKillMessage;
        return cosmetics;
    }

}
