package com.SirBlobman.expansion.citizens.config;

import org.bukkit.configuration.file.YamlConfiguration;

import com.SirBlobman.combatlogx.config.Config;
import com.SirBlobman.expansion.citizens.CompatCitizens;

import java.io.File;

public class ConfigCitizens extends Config {
    private static YamlConfiguration config = new YamlConfiguration();

    public static void load() {
        File folder = CompatCitizens.FOLDER;
        File file = new File(folder, "citizens.yml");
        if (!file.exists()) copyFromJar("citizens.yml", folder);

        config = load(file);
    }
    
    public static <O> O getOption(String path, O defaultValue) {
        load();
        return get(config, path, defaultValue);
    }
}