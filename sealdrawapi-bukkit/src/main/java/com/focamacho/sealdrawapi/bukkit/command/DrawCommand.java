package com.focamacho.sealdrawapi.bukkit.command;

import com.focamacho.sealdrawapi.SealDrawAPI;
import com.focamacho.sealdrawapi.api.Paint;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DrawCommand extends com.focamacho.sealdrawapi.command.DrawCommand implements Listener {

    public DrawCommand(SealDrawAPI api) {
        super(api);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if(event.getMessage().startsWith("/sdwa")) {
            execute(event.getPlayer(), event.getMessage().replace("/sdwa ", "").split(" "));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Paint paint = api.getPaint(event.getPlayer());
        if(paint != null) paint.closePaint(event.getPlayer());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Paint paint = api.getPaint(event.getPlayer());
        if(paint != null && paint.isStopChat()) {
            event.setCancelled(true);
        }
    }

}
