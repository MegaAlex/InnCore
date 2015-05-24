package me.megaalex.inncore.factionsmisc;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;
import me.megaalex.inncore.config.FactionMiscConfig;

public class FactionsMiscManager extends Manager {

    @Override
    public String getEnableConfigName() {
        return "factionsmisc.enabled";
    }

    @Override
    public void onEnable() {
        InnCore plugin = InnCore.getInstance();
        FactionMiscConfig config = plugin.getConfigManager().getFactionMiscConfig();
        super.onEnable();

        // Register events
        InnCore.debug("Registering events");
        plugin.getServer().getPluginManager().registerEvents(new FactionMiscListener(config), plugin);
    }
}