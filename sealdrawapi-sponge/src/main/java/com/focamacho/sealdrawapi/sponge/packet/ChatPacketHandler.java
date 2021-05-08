package com.focamacho.sealdrawapi.sponge.packet;

import com.focamacho.sealdrawapi.api.AbstractPaint;
import com.focamacho.sealdrawapi.sponge.SealDrawAPISponge;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.api.entity.living.player.Player;

//Bloqueia o recebimento de mensagens caso o jogador
//possua um "editor" aberto.
public class ChatPacketHandler extends ChannelDuplexHandler {

    private final Player player;
    private final PacketHandler handler;

    public ChatPacketHandler(EntityPlayer player, PacketHandler handler) {
        this.player = (Player) player;
        this.handler = handler;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if(msg instanceof SPacketChat) {
            SPacketChat packet = (SPacketChat) msg;
            AbstractPaint paint = handler.api.getPaint(player);

            if(paint != null && paint.isStopChat() && !handler.getChatComponent(packet).contains("{\"action\":\"run_command\",\"value\":\"/sdwa")) {
                return;
            }
        }

        super.write(ctx ,msg, promise);
    }

}
