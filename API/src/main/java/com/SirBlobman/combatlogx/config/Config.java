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
        throw new UnsupportedOperationException("Not implemented");
    }

    protected static void copyFromJar(String fileName, File folder) {
        throw new UnsupportedOperationException("Not implemented");
    }

    protected static YamlConfiguration load(String name) {
        throw new UnsupportedOperationException("Not implemented");
    }

    protected static YamlConfiguration load(File file) {
        throw new UnsupportedOperationException("Not implemented");
    }

    protected static void save(YamlConfiguration config, File file) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Gets a config value of the same type as defaultValue {@link O}<br/>
     *
     * @param config       YamlConfiguration to use
     * @param path         String path to the option
     * @param defaultValue If the value does not exist, it will become this
     * @return The value at {@code path}, if it is null or not the same type, {@code defaultValue} will be returned
     */
    protected static <O> O get(YamlConfiguration config, String path, O defaultValue) {
        throw new UnsupportedOperationException("Not implemented");
    }
}