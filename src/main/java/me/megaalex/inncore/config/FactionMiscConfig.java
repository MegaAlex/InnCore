package me.megaalex.inncore.config;

import org.bukkit.configuration.ConfigurationSection;

public class FactionMiscConfig implements SubConfig {

    public String miningPerm;
    public boolean miningEnable;
    public boolean disableWorldMining;

    public boolean disableStrPotions;

    public boolean disableNaturalObsidian;
    public String obsidianNoticeMessage;

    public boolean fixReportRtsCmd;
    public boolean disableVillagerTrading;
    public boolean rightClickIronDoors;
    public boolean nerfXP;

    @Override
    public String getSubName() {
        return "factionsmisc";
    }

    @Override
    public void loadConfig(ConfigurationSection config) {

        disableStrPotions = config.getBoolean("disableStr2Potions", true);

        miningEnable = config.getBoolean("mining.enabled", true);
        miningPerm = config.getString("mining.permission", "inncraft.mining");
        disableWorldMining = config.getBoolean("mining.disableWorldMining", true);


        disableNaturalObsidian = config.getBoolean("obsidian.enabled", true);
        obsidianNoticeMessage = config.getString("obsidian.message", "&cYou can only get obsidian from the shop!");

        fixReportRtsCmd = config.getBoolean("fixReportRtsCmd", true);
        disableVillagerTrading = config.getBoolean("disableVillagerTrading", true);
        rightClickIronDoors = config.getBoolean("rightClickIronDoors", true);
        nerfXP = config.getBoolean("nerfXP", true);

    }
}
