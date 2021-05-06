package com.focamacho.sealdrawapi.bungee.packet;

import com.focamacho.sealdrawapi.bungee.SealDrawAPIBungee;
import com.focamacho.sealdrawapi.api.AbstractPaint;
import de.exceptionflug.protocolize.api.event.PacketReceiveEvent;
import de.exceptionflug.protocolize.api.event.PacketSendEvent;
import de.exceptionflug.protocolize.api.handler.PacketAdapter;
import de.exceptionflug.protocolize.api.protocol.Stream;
import net.md_5.bungee.protocol.packet.Chat;

//Bloqueia o recebimento de mensagens caso o jogador
//possua um "editor" aberto.
public class ChatPacketAdapter extends PacketAdapter<Chat> {

    public ChatPacketAdapter(Stream stream) {
        super(stream, Chat.class);
    }

    @Override
    public void receive(PacketReceiveEvent<Chat> event) {
        if(event.getPlayer() == null) return;
        AbstractPaint paint = SealDrawAPIBungee.api.getPaint(event.getPlayer());
        System.out.println(event.getPacket().getMessage());
        if(paint != null && paint.isStopChat() && !event.getPacket().getMessage().startsWith("/sdwa")) {
            event.setCancelled(true);
        }
    }

    @Override
    public void send(PacketSendEvent<Chat> event) {
        if(event.getPlayer() == null) return;
        AbstractPaint paint = SealDrawAPIBungee.api.getPaint(event.getPlayer());
        if(paint != null && paint.isStopChat() && !event.getPacket().getMessage().contains("{\"action\":\"run_command\",\"value\":\"/sdwa")) {
            event.setCancelled(true);
        }
    }
}
