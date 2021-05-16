package com.focamacho.sealdrawapi.command;

import com.focamacho.sealdrawapi.SealDrawAPI;
import com.focamacho.sealdrawapi.api.Paint;
import com.focamacho.sealdrawapi.api.lib.PaintButton;

public abstract class DrawCommand {

    protected final SealDrawAPI api;

    public DrawCommand(SealDrawAPI api) {
        this.api = api;
    }

    public void execute(Object player, String[] args) {
        Paint paint = api.getPaint(player);

        if(paint != null) {
            if(args.length == 3) {
                //Colorir um pixel
                if(args[0].equalsIgnoreCase("p")) {
                    try {
                        int row = Integer.parseInt(args[1]);
                        int column = Integer.parseInt(args[2]);
                        paint.setColor(row, column, paint.getSelectedColor(player));
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

}
