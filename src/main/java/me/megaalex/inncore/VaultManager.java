package me.megaalex.inncore;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.permission.Permission;

public class VaultManager extends Manager {

    private Permission permission = null;

    @Override
    public void onEnable() {
        super.onEnable();
        setupPermissions();
    }

    private boolean setupPermissions() {
        final InnCore plugin = InnCore.getInstance();
        RegisteredServiceProvider<Permission> permissionProvider =
                plugin.getServer().getServicesManager().getRegistration(
                        net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    public boolean addPlayerToGroup(final Player player, final String groupName) {
        final String[] groups = permission.getGroups();
        final List<String> permittedGroups = InnCore.getInstance().getConfigManager().npcPermittedGroups;

        if(!permittedGroups.contains(groupName)) {
            throw new IllegalArgumentException("Not permitted group " + groupName);
        }

        if(!ArrayUtils.contains(groups, groupName)) {
            throw new IllegalArgumentException("Invalid group " + groupName);
        }

        return permission.playerAddGroup(player, groupName);
    }
}
