package com.fakeplayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.fakeplayer.core.FakeManager;
import com.fakeplayer.ui.TabUi;
import com.fakeplayer.commands.FakePlayerCommand;

public class FakePlayerPlugin extends JavaPlugin implements Listener {
    private static FakePlayerPlugin instance;
    private FakeManager fakeManager;
    private TabUi tabUi;

    public static FakePlayerPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.fakeManager = new FakeManager(this);
        this.fakeManager.start();

        this.tabUi = new TabUi(this, fakeManager);

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new com.fakeplayer.ping.PingListener(fakeManager, this), this);

        getCommand("fakeplayer").setExecutor(new FakePlayerCommand(fakeManager, this));
        getCommand("fakeplayer").setTabCompleter(new FakePlayerCommand(fakeManager, this));

        // Update tab for all online players on reload
        for (Player p : Bukkit.getOnlinePlayers()) {
            tabUi.updateFor(p);
        }

        getLogger().info("FakePlayer enabled.");
    }

    @Override
    public void onDisable() {
        if (fakeManager != null) fakeManager.stop();
        getLogger().info("FakePlayer disabled.");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        tabUi.updateFor(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        // Nothing special for now
    }
}

