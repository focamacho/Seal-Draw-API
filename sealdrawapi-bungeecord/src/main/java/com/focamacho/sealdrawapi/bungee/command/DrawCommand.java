package com.focamacho.sealdrawapi.bungee.command;

import com.focamacho.sealdrawapi.SealDrawAPI;
import com.focamacho.sealdrawapi.api.Paint;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class DrawCommand extends com.focamacho.sealdrawapi.command.DrawCommand implements Listener {

    public DrawCommand(SealDrawAPI api) {
        super(api);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(ChatEvent event) {
        if(event.getMessage().startsWith("/sdwa")) {
            if(!(event.getSender() instanceof CommandSender)) return;
            execute(event.getSender(), event.getMessage().replace("/sdwa ", "").split(" "));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {
        Paint paint = api.getPaint(event.getPlayer());
        if(paint != null) paint.closePaint(event.getPlayer());
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        Paint paint = api.getPaint(event.getSender());
        if(paint != null && paint.isStopChat()) {
            event.setCancelled(true);
        }
    }

}
