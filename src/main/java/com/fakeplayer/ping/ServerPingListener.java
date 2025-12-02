package com.fakeplayer.ping;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.fakeplayer.core.FakeManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ServerPingListener implements Listener {
    private final FakeManager fakeManager;

    public ServerPingListener(FakeManager fakeManager) {
        this.fakeManager = fakeManager;
    }

    @EventHandler
    public void onPing(PaperServerListPingEvent event) {
        int real = Bukkit.getOnlinePlayers().size();
        int fake = fakeManager.getFakePlayerCount();
        int total = real + fake;
        event.setNumPlayers(total);
        // Optional: ensure max >= total so client doesn't cap display
        if (event.getMaxPlayers() < total) {
            event.setMaxPlayers(total);
        }
    }
}

