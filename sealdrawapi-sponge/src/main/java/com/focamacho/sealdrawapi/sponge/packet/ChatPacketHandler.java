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

    public ChatPacketHandler(EntityPlayer player) {
        this.player = (Player) player;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if(msg instanceof SPacketChat) {
            SPacketChat packet = (SPacketChat) msg;
            AbstractPaint paint = SealDrawAPISponge.api.getPaint(player);

            if(paint != null && paint.isStopChat() && !getChatComponent(packet).contains("{\"action\":\"run_command\",\"value\":\"/sdwa")) {
                return;
            }
        }

        super.write(ctx ,msg, promise);
    }

    private static String getChatComponent(SPacketChat packet) {
        try {
            PacketHandler.chatComponentField.setAccessible(true);
            return ITextComponent.Serializer.componentToJson((ITextComponent) PacketHandler.chatComponentField.get(packet)).toLowerCase();
        } catch(Exception ignored) {
            return "";
        }
    }

}
