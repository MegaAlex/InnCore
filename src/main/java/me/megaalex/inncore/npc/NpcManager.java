package me.megaalex.inncore.npc;

import java.util.logging.Level;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;

public class NpcManager extends Manager {

    @Override
    public String getEnableConfigName() {
        return "npc.enabled";
    }

    @Override
    public void onEnable() {
        super.onEnable();
        final InnCore plugin = InnCore.getInstance();

        if(plugin.getServer().getPluginManager().getPlugin("Citizens") == null ||
                !plugin.getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
            plugin.getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled. NPCs disabled!");
            onDisable();
            return;
        }

        CitizensAPI.getTraitFactory().registerTrait(
                TraitInfo.create(RaceSelectTrait.class).withName(RaceSelectTrait.TRAIT_NAME));
        CitizensAPI.getSpeechFactory().register(RaceSelectVocalChord.class, RaceSelectVocalChord.CHORD_NAME);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
