package com.fakeplayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
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

    public FakeManager getFakeManager() {
        return fakeManager;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        // Require ProtocolLib to inject players into the tab list
        Plugin protocolLib = Bukkit.getPluginManager().getPlugin("ProtocolLib");
        if (protocolLib == null || !protocolLib.isEnabled()) {
            getLogger().severe("ProtocolLib is not installed or not enabled. Please install ProtocolLib to use FakePlayer.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.fakeManager = new FakeManager(this);
        this.tabUi = new TabUi(this, fakeManager);
        this.fakeManager.setTabUi(tabUi);
        this.fakeManager.start();

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new com.fakeplayer.ping.PingListener(fakeManager, this), this);
        Bukkit.getPluginManager().registerEvents(new com.fakeplayer.ping.ServerPingListener(fakeManager), this);

        FakePlayerCommand command = new FakePlayerCommand(fakeManager, this);
        getCommand("fakeplayer").setExecutor(command);
        getCommand("fakeplayer").setTabCompleter(command);

        // Đồng bộ tab list cho tất cả người chơi đang online (reload plugin)
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
        if (tabUi != null) {
            tabUi.clearViewer(e.getPlayer().getUniqueId());
        }
    }
}

