package com.fakeplayer.core;

import com.fakeplayer.ui.TabUi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

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

    // Thêm 1 fake player đảm bảo không trùng tên (case-insensitive) với real hoặc fake
    public boolean addFakePlayer(String name) {
        if (fakePlayers.size() >= maxFakePlayers) return false;
        String lower = name.toLowerCase();
        Set<String> reservedLower = buildReservedLower();
        if (reservedLower.contains(lower)) return false;
        fakePlayers.add(name);
        if (tabUi != null) tabUi.updateAll();
        return true;
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

    // Thêm N fake player với tên unique (case-insensitive)
    public void addRandomFakePlayers(int count) {
        Set<String> reservedLower = buildReservedLower();
        int toAdd = Math.min(count, maxFakePlayers - fakePlayers.size());
        for (int i = 0; i < toAdd; i++) {
            String unique = nameGenerator.generateUniqueName(reservedLower);
            fakePlayers.add(unique);
            reservedLower.add(unique.toLowerCase());
        }
        if (tabUi != null) tabUi.updateAll();
    }

    public void clearAllFakePlayers() {
        fakePlayers.clear();
        if (tabUi != null) tabUi.updateAll();
    }

    private void rotateFakePlayers() {
        if (fakePlayers.isEmpty()) return;
        int target = fakePlayers.size();
        Set<String> reservedLower = buildReservedLower();
        // Loại bỏ các tên cũ khỏi reservedLower trước khi tạo mới
        for (String old : fakePlayers) reservedLower.remove(old.toLowerCase());

        Set<String> newNames = new HashSet<>();
        while (newNames.size() < target) {
            String cand = nameGenerator.generateUniqueName(reservedLower);
            newNames.add(cand);
            reservedLower.add(cand.toLowerCase());
        }
        fakePlayers.clear();
        fakePlayers.addAll(newNames);
        if (tabUi != null) tabUi.updateAll();
    }

    public boolean isFakePlayer(String name) {
        return fakePlayers.stream().anyMatch(n -> n.equalsIgnoreCase(name));
    }

    private Set<String> buildReservedLower() {
        Set<String> reserved = fakePlayers.stream().map(String::toLowerCase).collect(Collectors.toSet());
        for (Player p : Bukkit.getOnlinePlayers()) {
            reserved.add(p.getName().toLowerCase());
        }
        return reserved;
    }
}

