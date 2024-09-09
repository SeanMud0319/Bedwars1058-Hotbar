package com.nontage.commands;

import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.nontage.Hotbar;
import com.nontage.listeners.ShopListener;
import com.nontage.menus.HotbarMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class test implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (strings.length == 0) {
                HotbarMenu.open((Player) commandSender);
            } else if (strings.length == 1) {
                if (strings[0].equalsIgnoreCase("debug")) {
                    if (!ShopListener.a.containsKey(player.getUniqueId())) {
                        ShopListener.a.put(player.getUniqueId(),true);
                        player.sendMessage("1");
                        return true;
                    }
                    if (!ShopListener.a.get(player.getUniqueId())) {
                        ShopListener.a.put(player.getUniqueId(),true);
                        player.sendMessage("1");
                    } else {
                        ShopListener.a.put(player.getUniqueId(), false);
                        player.sendMessage("2");
                    }
                } else if (strings[0].equalsIgnoreCase("getteam")) {
                    for (int i = 0; i < Hotbar.bw.getArenaUtil().getArenaByPlayer(player).getTeam(player).getSwordsEnchantments().size(); i++) {
                        player.sendMessage(Hotbar.bw.getArenaUtil().getArenaByPlayer(player).getTeam(player).getSwordsEnchantments().get(i).getEnchantment().toString());
                    }
                } else if (strings[0].equalsIgnoreCase("item")) {
                    ItemStack a = player.getItemInHand().clone();
                    player.getInventory().addItem(a);
                } else {
                    String path = strings[0];
                    ConfigurationSection section = Hotbar.bw.getConfigs().getShopConfig().getYml().getConfigurationSection(path);

                    if (section == null) {
                        player.sendMessage("The path does not exist or is not a valid section.");
                    } else {
                        player.sendMessage("Keys under " + path + ":");
                        for (String key : section.getKeys(false)) {
                            player.sendMessage("- " + key);
                        }
                    }
                }
            }
        }
        return true;
    }
}
