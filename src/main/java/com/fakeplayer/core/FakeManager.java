package com.fakeplayer.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class FakeManager {
    private final JavaPlugin plugin;
    private final Set<String> fakePlayers = Collections.synchronizedSet(new HashSet<>());
    private final NameGenerator nameGenerator;
    private int updateTaskId = -1;
    private int rotateTaskId = -1;
    private final int maxFakePlayers;
    private final int rotateInterval;

    public FakeManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.nameGenerator = new NameGenerator();
        this.maxFakePlayers = plugin.getConfig().getInt("max-fake-players", 50);
        this.rotateInterval = plugin.getConfig().getInt("rotate-interval", 300); // 5 minutes default
    }

    public void start() {
        // Task to rotate fake player names every X seconds
        rotateTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (!fakePlayers.isEmpty()) {
                rotateFakePlayers();
            }
        }, rotateInterval * 20L, rotateInterval * 20L);

        plugin.getLogger().info("FakeManager started with " + maxFakePlayers + " max fake players");
    }

    public void stop() {
        if (updateTaskId != -1) Bukkit.getScheduler().cancelTask(updateTaskId);
        if (rotateTaskId != -1) Bukkit.getScheduler().cancelTask(rotateTaskId);
        fakePlayers.clear();
    }

    public void addFakePlayer(String name) {
        if (fakePlayers.size() >= maxFakePlayers) {
            return;
        }
        fakePlayers.add(name);
    }

    public void removeFakePlayer(String name) {
        fakePlayers.remove(name);
    }

    public Set<String> getFakePlayers() {
        return new HashSet<>(fakePlayers);
    }

    public int getFakePlayerCount() {
        return fakePlayers.size();
    }

    public void addRandomFakePlayers(int count) {
        for (int i = 0; i < count && fakePlayers.size() < maxFakePlayers; i++) {
            String name = nameGenerator.generateRandomName();
            fakePlayers.add(name);
        }
    }

    public void clearAllFakePlayers() {
        fakePlayers.clear();
    }

    private void rotateFakePlayers() {
        Set<String> oldNames = new HashSet<>(fakePlayers);
        fakePlayers.clear();
        
        for (String ignored : oldNames) {
            String newName = nameGenerator.generateRandomName();
            fakePlayers.add(newName);
        }

        // Update tab for all players
        for (Player p : Bukkit.getOnlinePlayers()) {
            com.fakeplayer.FakePlayerPlugin.getInstance().getServer().getPluginManager()
                    .callEvent(new org.bukkit.event.player.PlayerJoinEvent(p, ""));
        }
    }

    public boolean isFakePlayer(String name) {
        return fakePlayers.contains(name);
    }
}

