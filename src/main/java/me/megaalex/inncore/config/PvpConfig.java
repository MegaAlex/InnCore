package me.megaalex.inncore.config;

public class PvpConfig {

    private boolean enabled;

    private String countTable;
    private String historyTable;

    private boolean useScoreboard;
    private long scoreDelay;
    private long scoreTime;
    private long scoreUpdate;
    private int scorePlayers;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getCountTable() {
        return countTable;
    }

    public void setCountTable(String countTable) {
        this.countTable = countTable;
    }

    public String getHistoryTable() {
        return historyTable;
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
}
