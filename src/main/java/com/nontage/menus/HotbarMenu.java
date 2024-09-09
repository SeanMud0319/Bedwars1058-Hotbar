package com.nontage.menus;

import com.nontage.Hotbar;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import top.speedcubing.lib.bukkit.inventory.ItemBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class HotbarMenu {
    public static final String[] HOTBAR_ITEM_LORE_ABOVE = {"§7Drag this to a hotbar slot below to", "§7favor that slot when purchasing an", "§7item in this category or on spawn.", "", "§eClick to drag!"};
    public static final String[] HOTBAR_ITEM_LORE_UNDER = {"§7items will prioritize this slot!", "", "§eClick to remove!"};
    public static Map<UUID, Map<Integer, String>> hotbarCatch = new HashMap<>();

    public static void open(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, "§8Hotbar Manager");
        menu.setItem(10, new ItemBuilder(Objects.requireNonNull(Material.getMaterial("STAINED_CLAY")))
                .name("§aBlocks")
                .addLore(HOTBAR_ITEM_LORE_ABOVE)
                .build());
        menu.setItem(11, new ItemBuilder(Objects.requireNonNull(Material.getMaterial("GOLD_SWORD")))
                .name("§aMelee")
                .addLore(HOTBAR_ITEM_LORE_ABOVE)
                .build());
        menu.setItem(12, new ItemBuilder(Material.IRON_PICKAXE)
                .name("§aTools")
                .addLore(HOTBAR_ITEM_LORE_ABOVE).build());
        menu.setItem(13, new ItemBuilder(Material.BOW)
                .name("§aRanged")
                .addLore(HOTBAR_ITEM_LORE_ABOVE).build());
        menu.setItem(14, new ItemBuilder(Objects.requireNonNull(Material.getMaterial("BREWING_STAND_ITEM")))
                .name("§aPotions")
                .addLore(HOTBAR_ITEM_LORE_ABOVE).build());
        menu.setItem(15, new ItemBuilder(Material.TNT)
                .name("§aUtility")
                .addLore(HOTBAR_ITEM_LORE_ABOVE).build());
        for (int i = 18; i <= 26; i++) {
            String material = Hotbar.bw.getForCurrentVersion("STAINED_GLASS_PANE", "STAINED_GLASS_PANE", "BLACK_STAINED_GLASS_PANE");
            menu.setItem(i, new ItemBuilder(Objects.requireNonNull(Material.getMaterial(material))).durability(7)
                    .name("§8⬆ §7Categories")
                    .addLore("§8⬇ §7Hotbar").build());
        }
        for (int i = 36; i <= 44; i++) {
            String material = Hotbar.bw.getForCurrentVersion("STAINED_GLASS_PANE", "STAINED_GLASS_PANE", "BLACK_STAINED_GLASS_PANE");
            menu.setItem(i, new ItemBuilder(Objects.requireNonNull(Material.getMaterial(material))).durability(7)
                    .name("§8⬆ §7Hotbar").build());
        }
        menu.setItem(48, new ItemBuilder(Material.ARROW)
                .name("§aGo Back")
                .addLore("§7Go back to the previous menu.").build());
        menu.setItem(50, new ItemBuilder(Material.BARRIER)
                .name("§cReset to Default")
                .addLore("§7Reset your hotbar to the default.").build());

        //player settings
        menu.setItem(27, getHotbarItemStack(hotbarCatch.get(player.getUniqueId()).get(0)));
        menu.setItem(28, getHotbarItemStack(hotbarCatch.get(player.getUniqueId()).get(1)));
        menu.setItem(29, getHotbarItemStack(hotbarCatch.get(player.getUniqueId()).get(2)));
        menu.setItem(30, getHotbarItemStack(hotbarCatch.get(player.getUniqueId()).get(3)));
        menu.setItem(31, getHotbarItemStack(hotbarCatch.get(player.getUniqueId()).get(4)));
        menu.setItem(32, getHotbarItemStack(hotbarCatch.get(player.getUniqueId()).get(5)));
        menu.setItem(33, getHotbarItemStack(hotbarCatch.get(player.getUniqueId()).get(6)));
        menu.setItem(34, getHotbarItemStack(hotbarCatch.get(player.getUniqueId()).get(7)));
        menu.setItem(35, getHotbarItemStack(hotbarCatch.get(player.getUniqueId()).get(8)));
        player.openInventory(menu);
    }

    private static ItemStack getHotbarItemStack(String material) {
        switch (material.toLowerCase()) {
            case "blocks":
                return new ItemBuilder(Objects.requireNonNull(Material.getMaterial("STAINED_CLAY")))
                        .name("§aBlocks")
                        .addLore("§7Blocks " + HOTBAR_ITEM_LORE_UNDER[0])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[1])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[2])
                        .build();
            case "melee":
                return new ItemBuilder(Objects.requireNonNull(Material.getMaterial("GOLD_SWORD")))
                        .name("§aMelee")
                        .addLore("§7Melee " + HOTBAR_ITEM_LORE_UNDER[0])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[1])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[2])
                        .build();
            case "tools":
                return new ItemBuilder(Objects.requireNonNull(Material.IRON_PICKAXE))
                        .name("§aTools")
                        .addLore("§7Tools " + HOTBAR_ITEM_LORE_UNDER[0])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[1])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[2])
                        .build();
            case "ranged":
                return new ItemBuilder(Objects.requireNonNull(Material.BOW))
                        .name("§aRanged")
                        .addLore("§7Ranged " + HOTBAR_ITEM_LORE_UNDER[0])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[1])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[2])
                        .build();
            case "potions":
                return new ItemBuilder(Objects.requireNonNull(Material.getMaterial("BREWING_STAND_ITEM")))
                        .name("§aPotions")
                        .addLore("§7Potions " + HOTBAR_ITEM_LORE_UNDER[0])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[1])
                        .addLore(HOTBAR_ITEM_LORE_UNDER[2])
                        .build();
            case "utility":
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
