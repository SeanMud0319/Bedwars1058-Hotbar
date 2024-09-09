package com.nontage.listeners;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.events.gameplay.GameStateChangeEvent;
import com.nontage.menus.HotbarMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameEndListener implements Listener {
    @EventHandler
    public void onGameEnd(GameStateChangeEvent e) {
        if (e.getNewState() == GameState.restarting) {
            e.getArena().getPlayers().forEach(a -> {
                HotbarMenu.hotbarCatch.remove(a.getUniqueId());
            });
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        HotbarMenu.hotbarCatch.remove(e.getPlayer().getUniqueId());
    }
}
