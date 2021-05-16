package com.focamacho.sealdrawapi.bungee.packet;

import com.focamacho.sealdrawapi.SealDrawAPI;
import com.focamacho.sealdrawapi.api.Paint;
import de.exceptionflug.protocolize.api.event.PacketReceiveEvent;
import de.exceptionflug.protocolize.api.event.PacketSendEvent;
import de.exceptionflug.protocolize.api.handler.PacketAdapter;
import de.exceptionflug.protocolize.api.protocol.Stream;
import net.md_5.bungee.protocol.packet.Chat;

//Bloqueia o recebimento de mensagens caso o jogador
//possua um "editor" aberto.
public class ChatPacketAdapter extends PacketAdapter<Chat> {

    private final SealDrawAPI api;

    public ChatPacketAdapter(SealDrawAPI api, Stream stream) {
        super(stream, Chat.class);
        this.api = api;
    }

    @Override
    public void receive(PacketReceiveEvent<Chat> event) {
        if(event.getPlayer() == null) return;
        Paint paint = api.getPaint(event.getPlayer());
        System.out.println(event.getPacket().getMessage());
        if(paint != null && paint.isStopChat() && !event.getPacket().getMessage().startsWith("/sdwa")) {
            event.setCancelled(true);
        }
    }

    @Override
    public void send(PacketSendEvent<Chat> event) {
        if(event.getPlayer() == null) return;
        Paint paint = api.getPaint(event.getPlayer());
        if(paint != null && paint.isStopChat() && !event.getPacket().getMessage().contains("{\"action\":\"run_command\",\"value\":\"/sdwa")) {
            event.setCancelled(true);
        }
    }

}
