package com.fakeplayer.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import com.fakeplayer.core.FakeManager;
import com.fakeplayer.ui.TabUi;
import com.fakeplayer.FakePlayerPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FakePlayerCommand implements CommandExecutor, TabCompleter {
    private final FakeManager fakeManager;
    private final FakePlayerPlugin plugin;

    public FakePlayerCommand(FakeManager fakeManager, FakePlayerPlugin plugin) {
        this.fakeManager = fakeManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subCmd = args[0].toLowerCase();

        switch (subCmd) {
            case "add":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /fakeplayer add <count>", NamedTextColor.RED));
                    return true;
                }
                try {
                    int count = Integer.parseInt(args[1]);
                    fakeManager.addRandomFakePlayers(count);
                    sender.sendMessage(Component.text("Added " + count + " fake players!", NamedTextColor.GREEN));
                    updateAllTabs();
                } catch (NumberFormatException e) {
                    sender.sendMessage(Component.text("Invalid number!", NamedTextColor.RED));
                }
                break;

            case "remove":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /fakeplayer remove <name>", NamedTextColor.RED));
                    return true;
                }
                String name = args[1];
                fakeManager.removeFakePlayer(name);
                sender.sendMessage(Component.text("Removed fake player: " + name, NamedTextColor.GREEN));
                updateAllTabs();
                break;

            case "list":
                sender.sendMessage(Component.text("Fake Players (" + fakeManager.getFakePlayerCount() + "):", NamedTextColor.YELLOW));
                for (String fakeName : fakeManager.getFakePlayers()) {
                    sender.sendMessage(Component.text("  - " + fakeName, NamedTextColor.GRAY));
                }
                break;

            case "clear":
                fakeManager.clearAllFakePlayers();
                sender.sendMessage(Component.text("Cleared all fake players!", NamedTextColor.GREEN));
                updateAllTabs();
                break;

            case "reload":
                plugin.reloadConfig();
                sender.sendMessage(Component.text("Config reloaded!", NamedTextColor.GREEN));
                break;

            default:
                sendHelp(sender);
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Component.text("=== FakePlayer Commands ===", NamedTextColor.GOLD));
        sender.sendMessage(Component.text("/fakeplayer add <count> - Add fake players", NamedTextColor.GRAY));
        sender.sendMessage(Component.text("/fakeplayer remove <name> - Remove a fake player", NamedTextColor.GRAY));
        sender.sendMessage(Component.text("/fakeplayer list - List all fake players", NamedTextColor.GRAY));
        sender.sendMessage(Component.text("/fakeplayer clear - Clear all fake players", NamedTextColor.GRAY));
        sender.sendMessage(Component.text("/fakeplayer reload - Reload config", NamedTextColor.GRAY));
    }

    private void updateAllTabs() {
        TabUi tabUi = new TabUi(plugin, fakeManager);
        tabUi.updateAll();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("add", "remove", "list", "clear", "reload");
        }
        return new ArrayList<>();
    }
}

