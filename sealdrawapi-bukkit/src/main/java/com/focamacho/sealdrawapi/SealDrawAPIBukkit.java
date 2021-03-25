package com.focamacho.sealdrawapi;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.focamacho.sealdrawapi.api.AbstractPaint;
import com.focamacho.sealdrawapi.command.BukkitDrawCommand;
import com.focamacho.sealdrawapi.logger.BukkitDrawFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
public final class SealDrawAPIBukkit {

    public static SealDrawAPI api;

    public static void onEnable(SealDrawAPI instance) {
        api = instance;
        Bukkit.getPluginManager().registerEvents(new BukkitDrawCommand(), (Plugin) instance.getPlugin());
        ((Logger) LogManager.getRootLogger()).addFilter(new BukkitDrawFilter());

        //ProtocolLib
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter((Plugin) instance.getPlugin(), ListenerPriority.HIGHEST, PacketType.Play.Server.CHAT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                //Bloqueia o recebimento de mensagens caso o jogador
                //possua um "editor" aberto.
                if(event.getPacketType() == PacketType.Play.Server.CHAT) {
                    if(event.getPlayer() == null) return;
                    AbstractPaint paint = SealDrawAPIBukkit.api.getPaint(event.getPlayer());
                    if(paint != null && paint.isStopChat() && !event.getPacket().getChatComponents().read(0).getJson().contains("{\"action\":\"run_command\",\"value\":\"/sdwa")) {
                        event.setCancelled(true);
                    }
                }
            }
        });
    }

}
