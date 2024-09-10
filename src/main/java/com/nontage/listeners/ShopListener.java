package com.nontage.listeners;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.shop.IBuyItem;
import com.andrei1058.bedwars.api.arena.shop.ICategoryContent;
import com.andrei1058.bedwars.api.arena.team.TeamEnchant;
import com.andrei1058.bedwars.api.events.shop.ShopBuyEvent;
import com.andrei1058.bedwars.api.events.shop.ShopOpenEvent;
import com.andrei1058.bedwars.shop.ShopCache;
import com.andrei1058.bedwars.shop.ShopManager;
import com.andrei1058.bedwars.shop.main.CategoryContent;
import com.andrei1058.bedwars.shop.quickbuy.PlayerQuickBuyCache;
import com.nontage.Hotbar;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.andrei1058.bedwars.BedWars.nms;
import static com.nontage.menus.HotbarMenu.hotbarCatch;

public class ShopListener implements Listener {
    public static Map<UUID, Boolean> a = new HashMap<>();

    @EventHandler
    public void onShopBuy(ShopBuyEvent e) {
        Player player = e.getBuyer();
//        if (!a.containsKey(player.getUniqueId())) return;
//        if (!a.get(player.getUniqueId())) return;
        if (hotbarCatch.get(player.getUniqueId()) == null) {
            return;
        }
        if (hotbarCatch.values().stream().allMatch(Map::isEmpty)) {
            return;
        }

        ICategoryContent content = e.getCategoryContent();
        Material currency = content.getContentTiers().get(0).getCurrency();
        ItemStack buyItem;
        int price = content.getContentTiers().get(0).getPrice();

        if (content.getIdentifier().startsWith("armor")) return;
        if (content.getIdentifier().startsWith("melee") || content.getIdentifier().startsWith("blocks") || content.getIdentifier().startsWith("ranged") || content.getIdentifier().startsWith("potions") || content.getIdentifier().startsWith("utility")) {
            //original buy item
            buyItem = content.getContentTiers().get(0).getBuyItemsList().get(0).getItemStack();
            e.setCancelled(true);

            if (content.getIdentifier().endsWith("wool") || content.getIdentifier().endsWith("clay") || content.getIdentifier().endsWith("glass")) {
                buyItem = Hotbar.bw.getVersionSupport().colourItem(buyItem, e.getArena().getTeam(player));
                giveItem(player, buyItem, currency, price, content.getIdentifier());
                return;
            }
            if (content.getIdentifier().startsWith("ranged")) {
                ItemMeta meta = buyItem.getItemMeta();
                meta.spigot().setUnbreakable(true);
                buyItem.setItemMeta(meta);
            }
            if (content.getIdentifier().endsWith("sword")) {
                ItemMeta meta = buyItem.getItemMeta();
                assert meta != null;
                Map<Enchantment, Integer> buyItemEnchants = buyItem.getEnchantments();
                for (Enchantment ench : buyItemEnchants.keySet()) {
                    boolean hasEnch = false;
                    for (TeamEnchant teamEnch : e.getArena().getTeam(player).getSwordsEnchantments()) {
                        if (teamEnch.getEnchantment().equals(ench)) {
                            e.getArena().getTeam(player).getSwordsEnchantments();
                            hasEnch = true;
                            break;
                        }
                    }
                    if (!hasEnch) {
                        meta.removeEnchant(ench);
                    }
                }
                for (TeamEnchant teamEnch : e.getArena().getTeam(player).getSwordsEnchantments()) {
                    meta.addEnchant(teamEnch.getEnchantment(), teamEnch.getAmplifier(), true);
                }

                meta.spigot().setUnbreakable(true);
                buyItem.setItemMeta(meta);
                removeWoodSword(player);
                giveItem(player, buyItem, currency, price, content.getIdentifier());
                return;
            }
        }
        if (content.getIdentifier().startsWith("tools")) {
            ShopCache shopCache = ShopCache.getShopCache(player.getUniqueId());
            int tier = shopCache.getCachedItem(content.getIdentifier()) == null ? 0 : shopCache.getCachedItem(content.getIdentifier()).getTier();
            buyItem = content.getContentTiers().get(tier).getBuyItemsList().get(0).getItemStack();
            Map<Integer, String> data = hotbarCatch.get(player.getUniqueId());
            List<Integer> hotbarSlots = getHotbarSlots(data, content.getIdentifier());
            if (hotbarSlots.isEmpty()) {
                //original buy
                return;
            }
            int firstSlot = hotbarSlots.get(0);
            ItemStack currentItem = player.getInventory().getItem(firstSlot);
            if (hotbarSlots.size() == 1) {
                if (currentItem == null || currentItem.getType().equals(Material.AIR)) {
                    e.setCanceledUpgrade(true);
                    if (tier == 0) {
                        content.getContentTiers().get(tier).getBuyItemsList().get(0).give(player, e.getArena(), firstSlot);
                        shopCache.new CachedItem((CategoryContent) content);
                    } else {
                        shopCache.upgradeCachedItem((CategoryContent) content, firstSlot);
                        ShopManager.getShop().open(player, PlayerQuickBuyCache.getQuickBuyCache(player.getUniqueId()), true);
                    }
                } else {
                    if (!getItemId(currentItem).equals(getItemId(buyItem))) {
                        ItemStack temp = currentItem.clone();
                        player.getInventory().setItem(firstSlot, new ItemStack(Material.AIR));
                        e.setCanceledUpgrade(true);
                        if (tier == 0) {
                            content.getContentTiers().get(tier).getBuyItemsList().get(0).give(player, e.getArena(), firstSlot);
                            shopCache.new CachedItem((CategoryContent) content);
                        } else {
                            shopCache.upgradeCachedItem((CategoryContent) content, firstSlot);
                            ShopManager.getShop().open(player, PlayerQuickBuyCache.getQuickBuyCache(player.getUniqueId()), true);
                        }
                        player.getInventory().addItem(temp);
                        return;
                    }
                }
                return;
            }
            for (int slot : hotbarSlots) {
                ItemStack item = player.getInventory().getItem(slot);
                if (item == null || item.getType().equals(Material.AIR)) {
                    e.setCanceledUpgrade(true);
                    shopCache.upgradeCachedItem((CategoryContent) content, slot);
                    ShopManager.getShop().open(player, PlayerQuickBuyCache.getQuickBuyCache(player.getUniqueId()), true);
                    player.updateInventory();
                    return;
                } else if (!getItemId(item).equals(getItemId(buyItem))) {
                    ItemStack temp = item.clone();
                    player.getInventory().setItem(slot, new ItemStack(Material.AIR));
                    e.setCanceledUpgrade(true);
                    shopCache.upgradeCachedItem((CategoryContent) content, slot);
                    ShopManager.getShop().open(player, PlayerQuickBuyCache.getQuickBuyCache(player.getUniqueId()), true);
                    if (!nms.isTool(item)) {
                        player.getInventory().addItem(temp);
                    }
                    player.updateInventory();
                    return;
                }
            }
            return;
        }
        giveItem(player, content.getContentTiers().get(0).getBuyItemsList().get(0).getItemStack(), currency, price, content.getIdentifier());
    }

    @EventHandler
    public void onShopOpen(InventoryOpenEvent e) {
        Player player = (Player) e.getPlayer();
        if (QuickBuyListener.isShop(player, e.getView().getTitle())) {
            if (Hotbar.hotbarConfig.getData(player.getUniqueId().toString()) == null || Hotbar.hotbarConfig.getData(player.getUniqueId().toString()).isEmpty()) {
                Hotbar.hotbarConfig.createData(player.getUniqueId().toString());
                hotbarCatch.put(player.getUniqueId(), Hotbar.hotbarConfig.getData(player.getUniqueId().toString()));
            } else {
                hotbarCatch.put(player.getUniqueId(), Hotbar.hotbarConfig.getData(player.getUniqueId().toString()));
            }
        }
    }
    private void giveItem(Player player, ItemStack buyItem, Material currency, int price, String identifyer) {
        Hotbar.bw.getShopUtil().takeMoney(player, currency, price);
        Map<Integer, String> data = hotbarCatch.get(player.getUniqueId());
        player.playSound(player.getLocation(), Sound.valueOf("NOTE_PLING"), 10, 2);
        List<Integer> hotbarSlots = getHotbarSlots(data, identifyer);
        if (hotbarSlots.isEmpty()) {
            player.getInventory().addItem(buyItem);
            return;
        }

        int firstSlot = hotbarSlots.get(0);
        ItemStack currentItem = player.getInventory().getItem(firstSlot);

        if (hotbarSlots.size() == 1) {
            if (currentItem == null || currentItem.getType().equals(Material.AIR)) {
                player.getInventory().setItem(firstSlot, buyItem);
            } else {
                if (!getItemId(currentItem).equals(getItemId(buyItem))) {
                    ItemStack temp = currentItem.clone();
                    player.getInventory().setItem(firstSlot, buyItem);
                    player.getInventory().addItem(temp);
                } else if (getItemId(currentItem).equals(getItemId(buyItem)) && nms.isSword(currentItem)) {
                    if (nms.getDamage(buyItem) >= nms.getDamage(currentItem)) {
                        ItemStack temp = currentItem.clone();
                        player.getInventory().setItem(firstSlot, buyItem);
                        player.getInventory().addItem(temp);
                    } else {
                        player.getInventory().addItem(buyItem);
                    }
                    return;
                } else {
                    player.getInventory().addItem(buyItem);
                }
            }
            return;
        }
        for (int slot : hotbarSlots) {
            ItemStack item = player.getInventory().getItem(slot);
            if (item == null || item.getType().equals(Material.AIR)) {
                player.getInventory().setItem(slot, buyItem);
                return;
            } else if (item.isSimilar(buyItem) && item.getAmount() < item.getMaxStackSize()) {
                player.getInventory().addItem(buyItem);
                return;
            } else if (!getItemId(item).equals(getItemId(buyItem))) {
                ItemStack temp = item.clone();
                player.getInventory().setItem(slot, buyItem);
                player.getInventory().addItem(temp);
                return;
            } else if (getItemId(item).equals(getItemId(buyItem)) && nms.isSword(item)) {
                if (nms.getDamage(buyItem) >= nms.getDamage(item)) {
                    ItemStack temp = item.clone();
                    player.getInventory().setItem(slot, buyItem);
                    player.getInventory().addItem(temp);
                } else {
                    player.getInventory().addItem(buyItem);
                }
                return;
            }
        }

        player.getInventory().addItem(buyItem);
    }


    private List<Integer> getHotbarSlots(Map<Integer, String> data, String identifyer) {
        String[] identifyerParts = identifyer.split("-");
        String keyPart = identifyerParts.length > 0 ? identifyerParts[0] : identifyer;
        return data.entrySet()
                .stream()
                .filter(entry -> entry.getValue().contains(keyPart))
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());
    }


    private void removeWoodSword(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;
            if (item.getType().equals(Material.getMaterial("WOOD_SWORD"))) {
                player.getInventory().remove(item);
            }
        }
    }

    public static String getItemId(ItemStack item) {
        if (item == null || item.getType().equals(Material.AIR)) return "";
        if (same(item, Material.getMaterial("WOOL"), Material.getMaterial("STAINED_CLAY"), Material.getMaterial("STAINED_GLASS"),
                Material.getMaterial("WOOD"), Material.getMaterial("ENDER_STONE"), Material.LADDER, Material.OBSIDIAN)) {
            return "blocks";
        }
        if (Hotbar.bw.getVersionSupport().isSword(item)) {
            return "melee";
        }
        if (item.getType().name().endsWith("_PICKAXE") || item.getType().name().endsWith("_AXE") || item.getType().name().endsWith("SHEARS")) {
            return "tools";
        }
        if (Hotbar.bw.getVersionSupport().isBow(item)) {
            return "ranged";
        }
        if (item.getType().equals(Material.POTION)) {
            return "potions";
        }
        if (same(item, Material.GOLDEN_APPLE, Hotbar.bw.getVersionSupport().materialSnowball(), Material.getMaterial("MONSTER_EGG"), Hotbar.bw.getVersionSupport().materialFireball(),
                Material.TNT, Material.ENDER_PEARL, Material.WATER_BUCKET, Material.EGG, Material.MILK_BUCKET, Material.SPONGE, Material.CHEST)) {
            return "utility";
        }
        return "";
    }
    public static boolean same(ItemStack item1, Material... materials) {
        if (item1 == null || materials == null) return false;
        for (Material material : materials) {
            if (material != null && item1.getType().equals(material)) {
                return true;
            }
        }
        return false;
    }



}
