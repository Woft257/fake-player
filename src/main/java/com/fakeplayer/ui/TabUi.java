package com.fakeplayer.ui;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.fakeplayer.core.FakeManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TabUi {
    private final JavaPlugin plugin;
    private final FakeManager fakeManager;
    private final ProtocolManager protocolManager;

    // Theo dõi trạng thái đã gửi cho từng người chơi (để chỉ gửi diff)
    private final Map<UUID, Set<String>> viewerShown = new ConcurrentHashMap<>();

    public TabUi(JavaPlugin plugin, FakeManager fakeManager) {
        this.plugin = plugin;
        this.fakeManager = fakeManager;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    // Đồng bộ tab list của một người chơi với danh sách fake hiện tại
    public void updateFor(Player viewer) {
        Set<String> current = fakeManager.getFakePlayers();
        Set<String> shown = viewerShown.computeIfAbsent(viewer.getUniqueId(), k -> ConcurrentHashMap.newKeySet());

        // Tính toán diff
        Set<String> toRemove = new HashSet<>(shown);
        toRemove.removeAll(current);

        Set<String> toAdd = new HashSet<>(current);
        toAdd.removeAll(shown);

        if (!toRemove.isEmpty()) sendRemove(viewer, toRemove);
        if (!toAdd.isEmpty()) sendAdd(viewer, toAdd);

        // Cập nhật trạng thái đã gửi
        shown.removeAll(toRemove);
        shown.addAll(toAdd);
    }

    // Gửi cập nhật cho tất cả người chơi online
    public void updateAll() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            updateFor(p);
        }
    }

    public void clearViewer(java.util.UUID viewerId) {
        viewerShown.remove(viewerId);
    }

    private void sendAdd(Player viewer, Collection<String> names) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);

        List<PlayerInfoData> list = names.stream().map(name -> {
            UUID uuid = stableUuid(name);
            WrappedGameProfile profile = new WrappedGameProfile(uuid, name);
            return new PlayerInfoData(
                    profile,
                    0, // ping
                    EnumWrappers.NativeGameMode.SURVIVAL,
                    (WrappedChatComponent) null
            );
        }).collect(Collectors.toList());

        packet.getPlayerInfoDataLists().write(0, list);
        trySend(viewer, packet);
    }

    private void sendRemove(Player viewer, Collection<String> names) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);

        List<PlayerInfoData> list = names.stream().map(name -> {
            UUID uuid = stableUuid(name);
            WrappedGameProfile profile = new WrappedGameProfile(uuid, name);
            return new PlayerInfoData(profile, 0, EnumWrappers.NativeGameMode.SURVIVAL, null);
        }).collect(Collectors.toList());

        packet.getPlayerInfoDataLists().write(0, list);
        trySend(viewer, packet);
    }

    private void trySend(Player viewer, PacketContainer packet) {
        try {
            protocolManager.sendServerPacket(viewer, packet);
        } catch (InvocationTargetException e) {
            plugin.getLogger().warning("Failed to send packet: " + e.getMessage());
        }
    }

    private static UUID stableUuid(String name) {
        return UUID.nameUUIDFromBytes(("FakePlayer-" + name).getBytes());
    }
}


