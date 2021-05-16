package com.focamacho.sealdrawapi.sponge.command;

import com.focamacho.sealdrawapi.SealDrawAPI;
import com.focamacho.sealdrawapi.api.Paint;
import com.focamacho.sealdrawapi.api.lib.PaintButton;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class DrawCommand extends com.focamacho.sealdrawapi.command.DrawCommand {

    public DrawCommand(SealDrawAPI api) {
        super(api);
    }

    @Listener(order = Order.PRE)
    public void onCommand(SendCommandEvent event) {
        if(event.getCommand().startsWith("sdwa")) {
            if(!(event.getSource() instanceof CommandSource)) return;
            execute(event.getSource(), event.getArguments().split(" "));
            event.setCancelled(true);
        }
    }

    @Listener
    public void onQuit(ClientConnectionEvent.Disconnect event) {
        Paint paint = api.getPaint(event.getTargetEntity());
        if(paint != null) paint.closePaint(event.getTargetEntity());
    }

    @Listener
    public void onChat(MessageChannelEvent.Chat event) {
        if(event.getSource() instanceof Player) {
            Paint paint = api.getPaint(event.getSource());
            if (paint != null && paint.isStopChat()) {
                event.setCancelled(true);
            }
        }
    }

}
