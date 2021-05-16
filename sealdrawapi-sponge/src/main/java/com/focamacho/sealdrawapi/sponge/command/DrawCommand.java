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

public class DrawCommand {

    private SealDrawAPI api;

    public DrawCommand(SealDrawAPI api) {
        this.api = api;
    }

    public void execute(CommandSource sender, String[] args) {
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
