package com.nontage.listeners;

import com.nontage.Hotbar;
import com.nontage.menus.HotbarMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import top.speedcubing.lib.bukkit.inventory.ItemBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.nontage.menus.HotbarMenu.HOTBAR_ITEM_LORE_UNDER;
import static com.nontage.menus.HotbarMenu.hotbarCatch;


public class HotbarListener implements Listener {
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
        if (!e.getView().getTitle().equals("§8Hotbar Manager")) return;
        e.setCancelled(true);
        switch (e.getRawSlot()) {
            //this is player settings for remove
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
                if (e.getCursor() != null && !e.getCursor().getType().equals(Material.AIR)) {
                    e.getInventory().setItem(e.getRawSlot(), e.getCursor());
                    e.getView().setCursor(new ItemStack(Material.AIR));
                    return;
                }
                if (clickedItem == null || clickedItem.getType().equals(Material.AIR) || clickedItem.getItemMeta() == null)
                    return;
                if (clickedItem.getItemMeta().getLore() == null) return;
                if (!clickedItem.getItemMeta().getLore().contains("§eClick to remove!")) return;
                e.getInventory().setItem(e.getRawSlot(), new ItemStack(Material.AIR));
                break;

            //this is player settings for add
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                assert clickedItem != null;
                e.getView().setCursor(updateLore(clickedItem.getType().name()));
                break;

            //go back
            case 48:
                player.performCommand("bw shopmenu");
                break;
            case 50:
                //make it just clear 27~35 then when inv close save data to yml
                e.getView().setItem(27, updateLore("GOLD_SWORD"));
                for (int i = 28; i <= 35; i++) {
                    e.getInventory().setItem(i, new ItemStack(Material.AIR));
                }
                break;
            default:
                if (e.getCursor() != null && !e.getCursor().getType().equals(Material.AIR)) {
                    player.setItemOnCursor(new ItemStack(Material.AIR));
                }
        }
    }

    @EventHandler
    public void onCloseInv(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        if (!e.getView().getTitle().equals("§8Hotbar Manager")) return;
        Map<Integer, String> data = new HashMap<>();
        for (int i = 27; i <= 35; i++) {
            data.put(i - 27, toDataFormat(e.getInventory().getItem(i)));
        }
        if (data.equals(hotbarCatch.get(player.getUniqueId()))) {
            return;
        } else {
            hotbarCatch.put(player.getUniqueId(), data);
            Hotbar.hotbarConfig.updateData(player.getUniqueId().toString(), data);
        }
    }


    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getItemMeta() == null || e.getItemDrop().getItemStack().getType().equals(Material.AIR))
            return;
        if (e.getItemDrop().getItemStack().getItemMeta().getLore() == null) return;
        if (e.getItemDrop().getItemStack().getItemMeta().getLore().contains(HotbarMenu.HOTBAR_ITEM_LORE_ABOVE[0])
                || e.getItemDrop().getItemStack().getItemMeta().getLore().contains(HOTBAR_ITEM_LORE_UNDER[2])) {
            e.getItemDrop().remove();
        }
    }

    private String toDataFormat(ItemStack item) {
        if (item == null || item.getType().equals(Material.AIR) || item.getItemMeta() == null) return "";
        switch (item.getType().name()) {
            case "STAINED_CLAY":
                return "blocks";
            case "GOLD_SWORD":
                return "melee";
            case "IRON_PICKAXE":
                return "tools";
            case "BOW":
                return "ranged";
            case "BREWING_STAND_ITEM":
                return "potions";
            case "TNT":
                return "utility";
            default:
                return "";

        }
    }

    private ItemStack updateLore(String material) {
        switch (material.toLowerCase()) {
            case "stained_clay":
                return new ItemBuilder(Objects.requireNonNull(Material.getMaterial("STAINED_CLAY")))
                        .name("§aBlocks")
                        .addLore("§7Blocks " + HOTBAR_ITEM_LORE_UNDER[0])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[1])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[2])
                        .build();
            case "gold_sword":
                return new ItemBuilder(Objects.requireNonNull(Material.getMaterial("GOLD_SWORD")))
                        .name("§aMelee")
                        .addLore("§7Melee " + HOTBAR_ITEM_LORE_UNDER[0])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[1])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[2])
                        .build();
            case "iron_pickaxe":
                return new ItemBuilder(Objects.requireNonNull(Material.IRON_PICKAXE))
                        .name("§aTools")
                        .addLore("§7Tools " + HOTBAR_ITEM_LORE_UNDER[0])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[1])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[2])
                        .build();
            case "bow":
                return new ItemBuilder(Material.BOW)
                        .name("§aRanged")
                        .addLore("§7Ranged " + HOTBAR_ITEM_LORE_UNDER[0])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[1])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[2])
                        .build();
            case "brewing_stand_item":
                return new ItemBuilder(Objects.requireNonNull(Material.getMaterial("BREWING_STAND_ITEM")))
                        .name("§aPotions")
                        .addLore("§7Potions " + HOTBAR_ITEM_LORE_UNDER[0])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[1])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[2])
                        .build();
            case "tnt":
                return new ItemBuilder(Objects.requireNonNull(Material.TNT))
                        .name("§aUtility")
                        .addLore("§7Utility " + HOTBAR_ITEM_LORE_UNDER[0])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[1])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[2])
                        .build();
            default:
                return new ItemStack(Material.AIR);
        }
    }
}


