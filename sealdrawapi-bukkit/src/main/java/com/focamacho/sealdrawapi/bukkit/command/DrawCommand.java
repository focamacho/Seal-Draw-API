package com.focamacho.sealdrawapi.bukkit.command;

import com.focamacho.sealdrawapi.SealDrawAPI;
import com.focamacho.sealdrawapi.api.Paint;
import com.focamacho.sealdrawapi.api.lib.PaintButton;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DrawCommand implements Listener {

    private final SealDrawAPI api;

    public DrawCommand(SealDrawAPI api) {
        this.api = api;
    }

    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return;

        Player player = (Player) sender;
        Paint paint = api.getPaint(player);

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

                //Botões
                if(args[0].equalsIgnoreCase("b")) {
                    PaintButton button = paint.getButton(args[1]);
                    if(button != null) button.getAction().run(player, paint);
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
