package com.focamacho.sealdrawapi.sponge.packet;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.lang.reflect.Field;

public class PacketHandler {

    public static final Field chatComponentField;

    static {
        Field toAssign;

        try {
            toAssign = SPacketChat.class.getDeclaredField("field_148919_a");
        } catch(Exception ignored) {
            toAssign = null;
        }

        chatComponentField = toAssign;
    }

    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ((EntityPlayerMP) event.player).connection.netManager.channel().pipeline().addBefore("packet_handler", event.player.getName(), new ChatPacketHandler(event.player));
    }

    @SubscribeEvent
    public void onLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        ((EntityPlayerMP) event.player).connection.netManager.channel().pipeline().remove(event.player.getName());
    }

}
