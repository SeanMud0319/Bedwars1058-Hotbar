package com.nontage.configuration;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HotbarConfig {
    private final JavaPlugin plugin;
    private File file;
    private FileConfiguration config;

    public HotbarConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        setup();
    }

    public void setup() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        file = new File(plugin.getDataFolder(), "data.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void createData(String uuid) {
        Map<String, Object> data = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            if (i == 0) {
                data.put(String.valueOf(i), "Melee");
            } else {
                data.put(String.valueOf(i), "");
            }
        }

        config.createSection(uuid, data);
        saveConfig();
    }

    public void updateData(String uuid, Map<Integer, String> data) {
        ConfigurationSection section = config.getConfigurationSection(uuid);
        if (section != null) {
            for (Map.Entry<Integer, String> entry : data.entrySet()) {
                section.set(entry.getKey().toString(), entry.getValue());
            }
            saveConfig();
        }
    }

    public void resetData(String uuid) {
        Map<Integer, String> data = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            data.put(i, "melee");
        }
        updateData(uuid, data);
    }

    public Map<Integer, String> getData(String uuid) {
        ConfigurationSection section = config.getConfigurationSection(uuid);
        if (section != null) {
            Map<Integer, String> data = new HashMap<>();
            for (String key : section.getKeys(false)) {
                String value = section.getString(key);
                data.put(Integer.parseInt(key), value);
            }
            return data;
        }
        return null;
    }
}
