package com.fakeplayer.ui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import com.fakeplayer.core.FakeManager;

public class TabUi {
    private final JavaPlugin plugin;
    private final FakeManager fakeManager;

    public TabUi(JavaPlugin plugin, FakeManager fakeManager) {
        this.plugin = plugin;
        this.fakeManager = fakeManager;
    }

    public void updateFor(Player player) {
        int fakeCount = fakeManager.getFakePlayerCount();
        int realCount = Bukkit.getOnlinePlayers().size();
        int totalCount = realCount + fakeCount;

        // Update header and footer
        Component header = Component.text("FakePlayer Plugin", NamedTextColor.GOLD)
                .append(Component.newline())
                .append(Component.text("Real: " + realCount + " | Fake: " + fakeCount, NamedTextColor.GRAY));

        Component footer = Component.text("Total Players: " + totalCount, NamedTextColor.YELLOW)
                .append(Component.newline())
                .append(Component.text("Server is running smoothly!", NamedTextColor.GREEN));

        player.sendPlayerListHeaderAndFooter(header, footer);
    }

    public void updateAll() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            updateFor(p);
        }
    }
}

