package me.megaalex.inncore.config;

import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;

import me.megaalex.inncore.config.file.ConfigFile;

public class FactionMiscConfig implements SubConfig {

    public String miningPerm;

    public boolean miningEnable;
    public boolean disableWorldMining;
    public String mainWorldName;

    public boolean disableStrPotions;

    public boolean disableNaturalObsidian;
    public String obsidianNoticeMessage;

    public boolean fixReportRtsCmd;
    public boolean disableVillagerTrading;
    public boolean rightClickIronDoors;
    public boolean nerfXP;
    public boolean removeCheatedItems;

    public boolean enableMiningDivide;
    public int miningLevelSize;
    public HashMap<String, Integer> miningLevels;
    public HashMap<Integer, String> miningLevelNames;
    public HashMap<Integer, Integer> townRankZone;
    public ConfigFile miningConfig;

    private double miningSpawnX;
    private double miningSpawnZ;
    public boolean miningZoneTesting;

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
        mainWorldName = config.getString("mining.mainWorld", "world");


        disableNaturalObsidian = config.getBoolean("obsidian.enabled", true);
        obsidianNoticeMessage = config.getString("obsidian.message", "&cYou can only get obsidian from the shop!");

        fixReportRtsCmd = config.getBoolean("fixReportRtsCmd", true);
        disableVillagerTrading = config.getBoolean("disableVillagerTrading", true);
        rightClickIronDoors = config.getBoolean("rightClickIronDoors", true);
        nerfXP = config.getBoolean("nerfXP", true);

        removeCheatedItems = config.getBoolean("removeCheatedItems", true);

        enableMiningDivide = config.getBoolean("mining.divide", false);
        miningLevelSize = config.getInt("mining.levelSize", 2000);
        miningZoneTesting = config.getBoolean("mining.zoneTesting", true);
        miningLevels = new HashMap<>();
        miningLevelNames = new HashMap<>();
        townRankZone = new HashMap<>();
        miningConfig = new ConfigFile("miningConfig");
        if(enableMiningDivide) {
            for(String miningLvl: config.getConfigurationSection("mining.miningZone").getKeys(false)) {
                miningLevels.put(miningLvl, config.getInt("mining.miningZone." + miningLvl));
                miningLevelNames.put(config.getInt("mining.miningZone." + miningLvl), miningLvl);
            }

            for(String townRankString : config.getConfigurationSection("mining.townMiningZone").getKeys(false)) {
                int townRank = Integer.parseInt(townRankString);
                townRankZone.put(townRank, config.getInt("mining.townMiningZone." + townRankString));
            }
            miningSpawnX = miningConfig.getConfig().getDouble("spawn.x", 0.0);
            miningSpawnZ = miningConfig.getConfig().getDouble("spawn.z", 0.0);
        }
    }

    public double getSpawnX() {
        return miningSpawnX;
    }

    public double getSpawnZ() {
        return miningSpawnZ;
    }

    public void setSpawnX(int x) {
        miningConfig.getConfig().set("spawn.x", x);
        miningSpawnX = x;
        saveMiningConfig();
    }

    public void setSpawnZ(int z) {
        miningConfig.getConfig().set("spawn.z", z);
        miningSpawnZ = z;
        saveMiningConfig();
    }

    private void saveMiningConfig() {
        miningConfig.saveConfig();
    }
}
