package com.fakeplayer.core;

import com.fakeplayer.ui.TabUi;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class FakeManager {
    private final JavaPlugin plugin;
    private final Set<String> fakePlayers = Collections.synchronizedSet(new HashSet<>());
    private final NameGenerator nameGenerator;
    private int rotateTaskId = -1;
    private final int maxFakePlayers;
    private final int rotateInterval;
    private TabUi tabUi;

    public FakeManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.nameGenerator = new NameGenerator();
        this.maxFakePlayers = plugin.getConfig().getInt("max-fake-players", 50);
        this.rotateInterval = plugin.getConfig().getInt("rotate-interval", 300); // 5 minutes default
    }

    public void setTabUi(TabUi tabUi) {
        this.tabUi = tabUi;
    }

    public void start() {
        if (rotateInterval > 0) {
            rotateTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::rotateFakePlayers,
                    rotateInterval * 20L, rotateInterval * 20L);
        }
        plugin.getLogger().info("FakeManager started with " + maxFakePlayers + " max fake players");
    }

    public void stop() {
        if (rotateTaskId != -1) Bukkit.getScheduler().cancelTask(rotateTaskId);
        fakePlayers.clear();
    }

    public void addFakePlayer(String name) {
        if (fakePlayers.size() >= maxFakePlayers) return;
        fakePlayers.add(name);
        if (tabUi != null) tabUi.updateAll();
    }

    public void removeFakePlayer(String name) {
        if (fakePlayers.remove(name) && tabUi != null) tabUi.updateAll();
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
        if (tabUi != null) tabUi.updateAll();
    }

    public void clearAllFakePlayers() {
        fakePlayers.clear();
        if (tabUi != null) tabUi.updateAll();
    }

    private void rotateFakePlayers() {
        if (fakePlayers.isEmpty()) return;
        Set<String> oldNames = new HashSet<>(fakePlayers);
        fakePlayers.clear();
        for (String ignored : oldNames) {
            fakePlayers.add(nameGenerator.generateRandomName());
        }
        if (tabUi != null) tabUi.updateAll();
    }

    public boolean isFakePlayer(String name) {
        return fakePlayers.contains(name);
    }
}

