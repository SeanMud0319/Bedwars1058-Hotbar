package com.nontage;

import com.andrei1058.bedwars.api.BedWars;
import com.nontage.commands.test;
import com.nontage.configuration.HotbarConfig;
import com.nontage.listeners.GameEndListener;
import com.nontage.listeners.HotbarListener;
import com.nontage.listeners.QuickBuyListener;
import com.nontage.listeners.ShopListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Hotbar extends JavaPlugin {
    public static BedWars bw;
    public static Hotbar plugin;
    public static HotbarConfig hotbarConfig;
    @Override
    public void onEnable() {
        getLogger().info("Hotbar plugin enabled!");
        plugin = this;
        if (Bukkit.getPluginManager().getPlugin("BedWars1058") == null) {
            getLogger().severe("BedWars1058 was not found. Disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        var registration = Bukkit.getServicesManager().getRegistration(BedWars.class);
        if (null == registration) {
            getLogger().severe("Cannot hook into BedWars1058.");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }
        bw = registration.getProvider();
        getLogger().info("Hooked into BedWars1058!");

        registerListener();
        registerCommand();
        hotbarConfig = new HotbarConfig(this);
        hotbarConfig.setup();
    }

    @Override
    public void onDisable() {
        getLogger().info("Hotbar plugin disabled!");
    }
    private void registerCommand() {
        Bukkit.getPluginCommand("test").setExecutor(new test());
    }
    private void registerListener() {
        Bukkit.getPluginManager().registerEvents(new ShopListener(), this);
        Bukkit.getPluginManager().registerEvents(new QuickBuyListener(), this);
        Bukkit.getPluginManager().registerEvents(new HotbarListener(), this);
        Bukkit.getPluginManager().registerEvents(new GameEndListener(), this);
    }
}