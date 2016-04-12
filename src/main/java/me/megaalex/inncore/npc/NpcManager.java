package me.megaalex.inncore.npc;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;
import me.megaalex.inncore.config.NpcConfig;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;

public class NpcManager extends Manager {

    @Override
    public String getEnableConfigName() {
        return "npc.enabled";
    }

    private HashMap<String, StaffMemberTrait> staffMemberTraits;
    private NpcConfig npcConfig;

    @Override
    public void onEnable() {
        super.onEnable();
        final InnCore plugin = InnCore.getInstance();
        npcConfig = plugin.getConfigManager().getNpcConfig();
        staffMemberTraits = new HashMap<>();


        if(plugin.getServer().getPluginManager().getPlugin("Citizens") == null ||
                !plugin.getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
            plugin.getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled. NPCs disabled!");
            onDisable();
            return;
        }

        CitizensAPI.getTraitFactory().registerTrait(
                TraitInfo.create(RaceSelectTrait.class).withName(RaceSelectTrait.TRAIT_NAME));
        try {
            CitizensAPI.getSpeechFactory().register(RaceSelectVocalChord.class, RaceSelectVocalChord.CHORD_NAME);
        } catch (IllegalArgumentException ignored) {
            InnCore.getInstance().getLogger().warning("Couldn't register race select vocal chord");
        }

        if(npcConfig.staffMemberEnabled) {
            if(!plugin.getServer().getPluginManager().isPluginEnabled("HolographicDisplays")) {
                plugin.getLogger().warning("Staff member trait enabled but no HolographicDisplays found! Ignoring!");
                return;
            }
            CitizensAPI.getTraitFactory().registerTrait(
                    TraitInfo.create(StaffMemberTrait.class).withName(StaffMemberTrait.TRAIT_NAME));
            plugin.getServer().getPluginManager().registerEvents(new NpcEventListener(this), plugin);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        CitizensAPI.getTraitFactory().deregisterTrait(
                TraitInfo.create(RaceSelectTrait.class).withName(RaceSelectTrait.TRAIT_NAME));
        if(npcConfig.staffMemberEnabled) {
            CitizensAPI.getTraitFactory().deregisterTrait(
                    TraitInfo.create(StaffMemberTrait.class).withName(StaffMemberTrait.TRAIT_NAME));
        }
    }

    public void registerStaffTrait(String staffName, StaffMemberTrait trait) {
        staffMemberTraits.put(staffName.toLowerCase(), trait);
    }

    public void unregisterStaffTrait(String staffName) {
        staffMemberTraits.remove(staffName);
    }

    public void handlePlayerJoin(Player player) {
        sendStateUpdate(player.getName(), StaffMemberTrait.State.ONLINE);
    }

    public void handlePlayerLeave(Player player) {
        sendStateUpdate(player.getName(), StaffMemberTrait.State.OFFLINE);
    }

    public void sendStateUpdate(String player, StaffMemberTrait.State state) {
        StaffMemberTrait trait = staffMemberTraits.get(player.toLowerCase());
        if(trait != null) {
            trait.updateState(state);
        }
    }

    public NpcConfig getNpcConfig() {
        return npcConfig;
    }
}
