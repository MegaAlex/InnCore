/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.data;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.megaalex.inncore.InnCore;

public class CustomDataFile {

    private final File file;
    private FileConfiguration fileConfig;

    public CustomDataFile(File file) {
        this.file = file;
        initFile();
    }

    private void initFile() {
        if(!file.exists() || file.isFile()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveConfig() {
        if (file != null && fileConfig != null) {
            try {
                getConfig().save(file);
            } catch (IOException ex) {
                InnCore.getInstance().getLogger().log(Level.SEVERE,
                        "Could not save config to " + file, ex);
            }
        }
    }

    public FileConfiguration getConfig() {
        if (fileConfig == null) {
            this.reloadConfig();
        }
        return fileConfig;
    }

    public void reloadConfig() {
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }


}
