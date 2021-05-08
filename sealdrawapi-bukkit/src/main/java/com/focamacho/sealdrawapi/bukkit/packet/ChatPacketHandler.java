package com.focamacho.sealdrawapi.bukkit.packet;

import com.focamacho.sealdrawapi.api.AbstractPaint;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;

//Bloqueia o recebimento de mensagens caso o jogador
//possua um "editor" aberto.
public class ChatPacketHandler extends ChannelDuplexHandler {

    private final Player player;
    private final PacketHandler handler;

    public ChatPacketHandler(Player player, PacketHandler handler) {
        this.player = player;
        this.handler = handler;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if(handler.chatPacketClass.isInstance(msg)) {
            AbstractPaint paint = handler.api.getPaint(player);
            if(paint != null && paint.isStopChat() && !handler.getChatComponent(msg).contains("{\"action\":\"run_command\",\"value\":\"/sdwa")) {
                return;
            }
        }

        super.write(ctx ,msg, promise);
    }

}