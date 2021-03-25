package com.focamacho.sealdrawapi.command;

import com.focamacho.sealdrawapi.SealDrawAPIBukkit;
import com.focamacho.sealdrawapi.api.AbstractPaint;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitDrawCommand implements Listener {

    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return;

        Player player = (Player) sender;
        AbstractPaint paint = SealDrawAPIBukkit.api.getPaint(player);

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
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if(event.getMessage().startsWith("/sdwa")) {
            execute(event.getPlayer(), event.getMessage().replace("/sdwa ", "").split(" "));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        AbstractPaint paint = SealDrawAPIBukkit.api.getPaint(event.getPlayer());
        if(paint != null) paint.closePaint(event.getPlayer());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        AbstractPaint paint = SealDrawAPIBukkit.api.getPaint(event.getPlayer());
        if(paint != null && paint.isStopChat()) {
            event.setCancelled(true);
        }
    }

}
