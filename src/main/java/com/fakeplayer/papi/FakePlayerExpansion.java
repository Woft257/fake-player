package com.fakeplayer.papi;

import com.fakeplayer.FakePlayerPlugin;
import com.fakeplayer.core.FakeManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class FakePlayerExpansion extends PlaceholderExpansion {
    private final FakeManager manager;
    private final FakePlayerPlugin plugin;

    public FakePlayerExpansion(FakeManager manager, FakePlayerPlugin plugin) {
        this.manager = manager;
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "fakeplayer";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    // Keep registered on reload
    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        switch (params.toLowerCase()) {
            case "total":
                return String.valueOf(Bukkit.getOnlinePlayers().size() + manager.getFakePlayerCount());
            case "real":
                return String.valueOf(Bukkit.getOnlinePlayers().size());
            case "fake":
                return String.valueOf(manager.getFakePlayerCount());
            default:
                return null;
        }
    }
}

