package com.SirBlobman.combatlogx.config;

import com.SirBlobman.combatlogx.CombatLogX;
import com.SirBlobman.combatlogx.utility.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {
    protected static File getDataFolder() {
        CombatLogX plugin = JavaPlugin.getPlugin(CombatLogX.class);
        return plugin.getDataFolder();
    }

    protected static void copyFromJar(String fileName, File folder) {
        if(fileName == null) throw new IllegalArgumentException("fileName cannot be null!");
        if(folder == null) throw new IllegalArgumentException("folder cannot be null!");

        try {
            if(!folder.exists()) folder.mkdirs();
            File newFile = new File(folder, fileName);
            if(newFile.exists()) return;

            CombatLogX plugin = JavaPlugin.getPlugin(CombatLogX.class);
            InputStream inputStream = plugin.getResource("resources/" + fileName);
            if(inputStream == null) throw new IOException("Could not find '" + fileName + " in the class path.");

            Path path = newFile.toPath();
            Files.copy(inputStream, path);
        } catch(IOException ex) {
            Util.print("An error occurred while copying a default configuration file.");
            ex.printStackTrace();
        }
    }

    protected static YamlConfiguration load(String name) {
        File pluginFolder = getDataFolder();
        File file = new File(pluginFolder, name + ".yml");
        return load(file);
    }

    protected static YamlConfiguration load(File file) {
        try {
            YamlConfiguration config = new YamlConfiguration();
            if (!file.exists()) save(config, file);
            config.load(file);
            return config;
        } catch (Throwable ex) {
            String error = "Failed to load config '" + file + "':";
            Bukkit.getConsoleSender().sendMessage(error);
            ex.printStackTrace();
            return null;
        }
    }

    protected static void save(YamlConfiguration config, File file) {
        try {
            if (!file.exists()) {
                File pluginFolder = getDataFolder();
                pluginFolder.mkdirs();
                file.createNewFile();
            }
            config.save(file);
        } catch (Throwable ex) {
            String error = "Failed to save config '" + file + "':";
            Bukkit.getConsoleSender().sendMessage(error);
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    protected static <O> O get(YamlConfiguration config, String path, O defaultValue) {
        if(!config.isSet(path)) {
            config.set(path, defaultValue);
            return defaultValue;
        }

        Object object = config.get(path);
        Class<?> object_class = object.getClass();

        Class<?> defaultValue_class = defaultValue.getClass();
        if(defaultValue_class.isInstance(object) || defaultValue_class.isAssignableFrom(object_class)) {
            return (O) defaultValue_class.cast(object);
        }

        config.set(path, defaultValue);
        return defaultValue;
    }
}