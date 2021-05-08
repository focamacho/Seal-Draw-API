package com.focamacho.sealdrawapi.bukkit.packet;

import com.focamacho.sealdrawapi.SealDrawAPI;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Field;
import java.util.Map;

public class PacketHandler implements Listener {

    protected final SealDrawAPI api;

    private static final String nmsVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static final String nmsPackage = "net.minecraft.server." + nmsVersion;
    private static final String cbPackage = "org.bukkit.craftbukkit." + nmsVersion;

    protected final Class<?> chatPacketClass;
    protected final Field componentField;
    protected final Gson gson;

    private final Field handleField;
    private final Field connectionField;
    private final Field managerField;
    private final Field channelField;

    private final Map<Player, ChatPacketHandler> handlers = Maps.newConcurrentMap();

    public PacketHandler(SealDrawAPI api) throws ReflectiveOperationException {
        this.api = api;

        Class<?> craftPlayerClass = Class.forName(cbPackage + ".entity.CraftEntity");
        Class<?> nmsPlayerClass = Class.forName(nmsPackage + ".EntityPlayer");
        Class<?> playerConnectionClass = Class.forName(nmsPackage + ".PlayerConnection");
        Class<?> networkManagerClass = Class.forName(nmsPackage + ".NetworkManager");
        Class<?> chatSerializer = Class.forName(nmsPackage + ".IChatBaseComponent$ChatSerializer");

        handleField = craftPlayerClass.getDeclaredField("entity");
        connectionField = nmsPlayerClass.getDeclaredField("playerConnection");
        managerField = playerConnectionClass.getDeclaredField("networkManager");
        channelField = networkManagerClass.getDeclaredField("channel");

        chatPacketClass = Class.forName(nmsPackage + ".PacketPlayOutChat");
        componentField = chatPacketClass.getDeclaredField("a");

        Field gsonField = chatSerializer.getDeclaredField("a");
        gsonField.setAccessible(true);
        gson = (Gson) gsonField.get(null);
        gsonField.setAccessible(false);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Channel channel = getChannel(event.getPlayer());
        if(channel != null) {
            ChatPacketHandler handler = new ChatPacketHandler(event.getPlayer(), this);
            channel.pipeline().addBefore("packet_handler", event.getPlayer().getName(), handler);
            handlers.put(event.getPlayer(), handler);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ChatPacketHandler handler = handlers.remove(event.getPlayer());
        if(handler != null) {
            Channel channel = getChannel(event.getPlayer());
            if(channel != null) {
                channel.eventLoop().submit(() -> channel.pipeline().remove(event.getPlayer().getName()));
            }
        }
    }

    private Channel getChannel(Player player) {
        try {
            handleField.setAccessible(true);
            return (Channel) channelField.get(managerField.get(connectionField.get(handleField.get(player))));
        } catch(IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String getChatComponent(Object packet) {
        try {
            componentField.setAccessible(true);
            Object o = componentField.get(packet);
            return gson.toJson(o);
        } catch(IllegalAccessException e) {
            return "";
        }
    }

}