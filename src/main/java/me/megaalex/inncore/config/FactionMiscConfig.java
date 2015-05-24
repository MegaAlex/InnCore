package me.megaalex.inncore.config;

import org.bukkit.configuration.ConfigurationSection;

public class FactionMiscConfig implements SubConfig {

    public String miningPerm;
    public boolean miningEnable;

    public boolean disableStrPotions;

    public boolean disableNaturalObsidian;
    public String obsidianNoticeMessage;

    @Override
    public String getSubName() {
        return "factionsmisc";
    }

    @Override
    public void loadConfig(ConfigurationSection config) {

        disableStrPotions = config.getBoolean("disableStr2Potions", true);

        miningEnable = config.getBoolean("mining.enabled", true);
        miningPerm = config.getString("mining.permission", "inncraft.mining");

        disableNaturalObsidian = config.getBoolean("obsidian.enabled", true);
        obsidianNoticeMessage = config.getString("obsidian.message", "&cYou can only get obsidian from the shop!");
    }
}
