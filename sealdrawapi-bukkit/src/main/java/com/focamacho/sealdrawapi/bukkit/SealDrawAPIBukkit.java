package com.focamacho.sealdrawapi.bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.focamacho.sealdrawapi.SealDrawAPI;
import com.focamacho.sealdrawapi.api.AbstractPaint;
import com.focamacho.sealdrawapi.bukkit.command.DrawCommand;
import com.focamacho.sealdrawapi.bukkit.logger.DrawFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
public final class SealDrawAPIBukkit {

    public static void onEnable(SealDrawAPI instance) {
        Bukkit.getPluginManager().registerEvents(new DrawCommand(instance), (Plugin) instance.getPlugin());
        ((Logger) LogManager.getRootLogger()).addFilter(new DrawFilter());

        //ProtocolLib
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter((Plugin) instance.getPlugin(), ListenerPriority.HIGHEST, PacketType.Play.Server.CHAT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                //Bloqueia o recebimento de mensagens caso o jogador
                //possua um "editor" aberto.
                if(event.getPacketType() == PacketType.Play.Server.CHAT) {
                    if(event.getPlayer() == null) return;
                    AbstractPaint paint = instance.getPaint(event.getPlayer());
                    if(paint != null && paint.isStopChat() && !event.getPacket().getChatComponents().read(0).getJson().contains("{\"action\":\"run_command\",\"value\":\"/sdwa")) {
                        event.setCancelled(true);
                    }
                }
            }
        });
    }

}
