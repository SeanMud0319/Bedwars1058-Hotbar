package com.nontage.listeners;

import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.nontage.Hotbar;
import com.nontage.menus.HotbarMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import top.speedcubing.lib.bukkit.inventory.ItemBuilder;

public class QuickBuyListener implements Listener {
    public static HotbarMenu hotbarMenu;
    @EventHandler
    public void onShop(InventoryOpenEvent e) {
        Player player = (Player) e.getPlayer();
        if (!isShop(player, e.getView().getTitle())) {
            return;
        }
        ItemStack hotbar = new ItemBuilder(Material.BLAZE_POWDER)
                .name("§aHotbar Manager")
                .addLore("§7Edit preferred slots for your items")
                .addLore("§7per category.")
                .build();
        e.getInventory().setItem(53, hotbar);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (!isShop(player, e.getView().getTitle())) {
            return;
        }
        if (e.getRawSlot() == 53) {
            Bukkit.getScheduler().runTaskAsynchronously(Hotbar.plugin, () -> {
                player.closeInventory();
                HotbarMenu.open(player);
            });
        }

    }

    public static boolean isShop(Player player, String title) {
        return title.equalsIgnoreCase(Language.getMsg(player, Messages.SHOP_INDEX_NAME));
    }
}
