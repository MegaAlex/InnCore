package me.megaalex.inncore.config;

import org.bukkit.configuration.ConfigurationSection;

import me.megaalex.inncore.InnCore;

public class PvpConfig implements SubConfig {

    private boolean enabled;

    private String countTable;
    private String historyTable;

    private boolean useScoreboard;
    private long scoreDelay;
    private long scoreTime;
    private long scoreUpdate;
    private int scorePlayers;

    @Override
    public String getSubName() {
        return "pvp";
    }

    @Override
    public void loadConfig(final ConfigurationSection config) {
        this.enabled = config.getBoolean("enabled", false);
        this.countTable = config.getString("killsTable", "inncore_kills");
        this.historyTable = config.getString("historyTable", "inncore_kills-h");

        // PvP Scoreboard Settings
        this.useScoreboard = config.getBoolean("scoreboard.use", false);
        this.scoreDelay = config.getLong("scoreboard.delay", 6000L);
        this.scoreTime = config.getLong("scoreboard.time", 200L);
        this.scoreUpdate = config.getLong("scoreboard.update", 3600L);
        this.scorePlayers = config.getInt("scoreboard.players", 10);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getCountTable() {
        return getDBPrefix() + countTable;
    }

    public void setCountTable(String countTable) {
        this.countTable = countTable;
    }

    public String getHistoryTable() {
        return getDBPrefix() + historyTable;
    }

    public void setHistoryTable(String historyTable) {
        this.historyTable = historyTable;
    }

    public boolean useScoreboard() {
        return useScoreboard;
    }

    public void setUseScoreboard(boolean useScoreboard) {
        this.useScoreboard = useScoreboard;
    }

    public long getScoreDelay() {
        return scoreDelay;
    }

    public void setScoreDelay(long scoreDelay) {
        this.scoreDelay = scoreDelay;
    }

    public long getScoreTime() {
        return scoreTime;
    }

    public void setScoreTime(long scoreTime) {
        this.scoreTime = scoreTime;
    }

    public int getScorePlayers() {
        return scorePlayers;
    }

    public void setScorePlayers(int scorePlayers) {
        if(scorePlayers > 16) {
            scorePlayers = 16;
        } else if(scorePlayers < 1) {
            scorePlayers = 1;
        }
        this.scorePlayers = scorePlayers;
    }

    public long getScoreUpdate() {
        return scoreUpdate;
    }

    public void setScoreUpdate(long scoreUpdate) {
        this.scoreUpdate = scoreUpdate;
    }

    private String getDBPrefix() {
        return InnCore.getInstance().getConfigManager().databasePrefix;
    }
}
