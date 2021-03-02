package com.focamacho.sealdrawapi.command;

import com.focamacho.sealdrawapi.SealDrawAPISponge;
import com.focamacho.sealdrawapi.api.AbstractPaint;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class SpongeDrawCommand {

    public void execute(CommandSource sender, String[] args) {
        if(!(sender instanceof Player)) return;

        Player player = (Player) sender;
        AbstractPaint paint = SealDrawAPISponge.api.getPaint(player);

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

    @Listener(order = Order.PRE)
    public void onCommand(SendCommandEvent event) {
        if(event.getCommand().startsWith("sdwa")) {
            if(!(event.getSource() instanceof CommandSource)) return;
            execute((CommandSource) event.getSource(), event.getArguments().split(" "));
            event.setCancelled(true);
        }
    }

    @Listener
    public void onQuit(ClientConnectionEvent.Disconnect event) {
        AbstractPaint paint = SealDrawAPISponge.api.getPaint(event.getTargetEntity());
        if(paint != null) paint.closePaint(event.getTargetEntity());
    }

}
