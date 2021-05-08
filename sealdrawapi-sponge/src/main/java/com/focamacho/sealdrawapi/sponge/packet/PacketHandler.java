package com.focamacho.sealdrawapi.sponge.packet;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.spongepowered.api.entity.living.player.Player;

import java.lang.reflect.Field;
import java.util.Map;

public class PacketHandler {

    protected static final Field chatComponentField;
    private static final Map<Player, ChatPacketHandler> handlers = Maps.newConcurrentMap();

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
        Channel channel = ((EntityPlayerMP) event.player).connection.netManager.channel();

        ChatPacketHandler handler = new ChatPacketHandler(event.player);
        channel.pipeline().addBefore("packet_handler", event.player.getName(), handler);

        handlers.put((Player) event.player, handler);
    }

    @SubscribeEvent
    public void onLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        ChatPacketHandler handler = handlers.remove((Player) event.player);
        if(handler != null) {
            Channel channel = ((EntityPlayerMP) event.player).connection.netManager.channel();
            channel.eventLoop().submit(() -> channel.pipeline().remove(event.player.getName()));
        }
    }

}
