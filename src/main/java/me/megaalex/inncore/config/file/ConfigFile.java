/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.config.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.base.Charsets;
import me.megaalex.inncore.InnCore;

public class ConfigFile {
    private File configFile;
    private FileConfiguration fileConfiguration;

    public ConfigFile(String fileName) {
        createFile(fileName);
    }

    public ConfigFile(String fileName, boolean copyDefaults) {
        boolean created = createFile(fileName);
        if(copyDefaults && created) {
            InnCore.getInstance().saveResource(fileName + ".yml", true);
        }
        InputStreamReader reader = new InputStreamReader(InnCore.getInstance().getResource(fileName + ".yml"), Charsets.UTF_8);
        Configuration defaultConfig = YamlConfiguration.loadConfiguration(reader);
        getConfig().setDefaults(defaultConfig);
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private boolean createFile(String fileName) {
        this.configFile = new File(InnCore.getInstance().getDataFolder()
                + File.separator + fileName + ".yml");
        if(!configFile.isFile()) {
            try {
                configFile.getParentFile().mkdirs();
                return configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public void saveConfig() {
        if (fileConfiguration != null && configFile != null) {
            try {
                getConfig().save(configFile);
            } catch (IOException ex) {
                InnCore.getInstance().getLogger().log(Level.SEVERE,
                        "Could not save config to " + configFile, ex);
            }
        }
    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            this.reloadConfig();
        }
        return fileConfiguration;
    }

    public void reloadConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
    }
}
