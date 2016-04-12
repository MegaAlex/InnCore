/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.npc;

import org.bukkit.Location;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.config.NpcConfig;
import me.megaalex.inncore.messages.MessageUtils;
import me.megaalex.inncore.utils.PlayerUtils;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;

public class StaffMemberTrait extends Trait {

    public final static String TRAIT_NAME = "staffmember";

    public NpcManager manager;
    public NpcConfig config;

    public enum State {
        ONLINE,
        OFFLINE
    }

    // Persistent variables
    @Persist
    public String staffName = null;

    Hologram hologram = null;

    public StaffMemberTrait() {
        super(TRAIT_NAME);

        manager = InnCore.getInstance().getNpcManager();
        config = manager.getNpcConfig();
    }

    public void setStaffName(String staffName) {
        manager.unregisterStaffTrait(this.staffName);
        this.staffName = staffName;
        onSpawn();
    }


    @Override
    public void onSpawn() {
        if(staffName == null) {
            return;
        }

        Location hologramLoc;
        String hologramText;

        if(npc.isSpawned()) {
            hologramLoc = this.npc.getEntity().getLocation();
        } else {
            hologramLoc = this.npc.getStoredLocation();
        }
        hologramLoc.add(0, config.staffMemberYOffset, 0);
        if (PlayerUtils.isOnline(staffName)) {
            hologramText = config.staffMemberOnlineText;
        } else {
            hologramText = config.staffMemberOfflineText;
        }

        if(hologram == null || hologram.isDeleted()) {
            hologram = HologramsAPI.createHologram(InnCore.getInstance(), hologramLoc);
        }
        setHologramText(hologramText);
        manager.registerStaffTrait(staffName, this);
    }

    public void updateState(State state) {
        String hologramText;
        if(state == State.ONLINE) {
            hologramText = config.staffMemberOnlineText;
        } else {
            hologramText = config.staffMemberOfflineText;
        }
        setHologramText(hologramText);
    }

    private void setHologramText(String hologramText) {
        if(hologram == null) {
            return;
        }
        hologram.clearLines();
        hologram.appendTextLine(MessageUtils.formatMessage(hologramText));
    }

    @Override
    public void onDespawn() {
        if(hologram == null) {
            return;
        }
        hologram.delete();
        manager.unregisterStaffTrait(staffName);
    }
}
