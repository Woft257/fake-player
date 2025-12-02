package com.fakeplayer.ping;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.fakeplayer.core.FakeManager;
import com.fakeplayer.ui.TabUi;

public class PingListener implements Listener {
    private final FakeManager fakeManager;
    private final JavaPlugin plugin;

    public PingListener(FakeManager fakeManager, JavaPlugin plugin) {
        this.fakeManager = fakeManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        // Update tab list for the joining player
        TabUi tabUi = new TabUi(plugin, fakeManager);
        tabUi.updateFor(e.getPlayer());
    }
}

