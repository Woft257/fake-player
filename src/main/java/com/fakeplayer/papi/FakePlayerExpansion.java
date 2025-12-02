package com.fakeplayer.papi;

import com.fakeplayer.FakePlayerPlugin;
import com.fakeplayer.core.FakeManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public @Nullable String onRequest(@Nullable Player player, @NotNull String params) {
        // Supported placeholders:
        // %fakeplayer_total% -> real + fake
        // %fakeplayer_real%  -> real online players
        // %fakeplayer_fake%  -> fake players count
        switch (params.toLowerCase()) {
            case "total":
                return String.valueOf(Bukkit.getOnlinePlayers().size() + manager.getFakePlayerCount());
            case "real":
                return String.valueOf(Bukkit.getOnlinePlayers().size());
            case "fake":
                return String.valueOf(manager.getFakePlayerCount());
            default:
                return null; // unknown placeholder
        }
    }
}

