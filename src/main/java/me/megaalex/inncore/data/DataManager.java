/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.data;

import java.io.File;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;

public class DataManager extends Manager {

    public File DATA_FOLDER;

    @Override
    public void onEnable() {
        super.onEnable();

        InnCore plugin = InnCore.getInstance();
        DATA_FOLDER = new File(plugin.getDataFolder() + File.separator + "data");
        if(!DATA_FOLDER.mkdirs()) {
            plugin.getLogger().warning("Couldn't create the data folder!");
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public CustomDataFile getConfigFile(String fileName) {
        return new CustomDataFile(getFileFromName(fileName));
    }

    public File getFileFromName(String fileName) {
        return new File(DATA_FOLDER + File.separator + fileName);
    }
}
