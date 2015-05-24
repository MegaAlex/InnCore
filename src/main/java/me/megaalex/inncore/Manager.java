package me.megaalex.inncore;

public class Manager {

    private boolean isEnabled = false;

    public void onEnable() {
        isEnabled = true;
        InnCore.getInstance().getLogger().info("Module " + this.getClass().getSimpleName() + " loaded");
    }

    public String getEnableConfigName() {
        return null;
    }

    public void onDisable() {
        isEnabled = false;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
