package com.focamacho.sealdrawapi.bungee.command;

import com.focamacho.sealdrawapi.api.AbstractPaint;
import com.focamacho.sealdrawapi.bungee.SealDrawAPIBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class DrawCommand implements Listener {

    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) sender;
        AbstractPaint paint = SealDrawAPIBungee.api.getPaint(player);

        if(paint != null) {
            if(args.length == 3) {
                //Colorir um pixel
                if(args[0].equalsIgnoreCase("p")) {
                    try {
                        int row = Integer.parseInt(args[1]);
                        int column = Integer.parseInt(args[2]);
                        paint.getDrawing().setColor(row, column, paint.getSelectedColor(player));
                        paint.updatePaint();
                    } catch (NumberFormatException ignored) {}
                }
            } else if(args.length == 2) {
                //Trocar a cor selecionada
                if(args[0].equalsIgnoreCase("c")) {
                    try {
                        char color = args[1].charAt(0);
                        paint.setSelectedColor(player, color);
                        paint.updatePaint();
                    } catch (IndexOutOfBoundsException ignored) {}
                }

                //Trocar a cor selecionada
                if(args[0].equalsIgnoreCase("b")) {
                    if(args[1].equalsIgnoreCase("co")) {
                        paint.getOnConfirm().run(player, paint);
                    } else if(args[1].equalsIgnoreCase("ca")) {
                        paint.getOnCancel().run(player, paint);
                    } else if(args[1].equalsIgnoreCase("cl")) {
                        paint.getOnClean().run(player, paint);
                        paint.updatePaint();
                    }
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(ChatEvent event) {
        if(event.getMessage().startsWith("/sdwa")) {
            if(!(event.getSender() instanceof CommandSender)) return;
            execute((CommandSender) event.getSender(), event.getMessage().replace("/sdwa ", "").split(" "));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {
        AbstractPaint paint = SealDrawAPIBungee.api.getPaint(event.getPlayer());
        if(paint != null) paint.closePaint(event.getPlayer());
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        AbstractPaint paint = SealDrawAPIBungee.api.getPaint(event.getSender());
        if(paint != null && paint.isStopChat()) {
            event.setCancelled(true);
        }
    }

}
