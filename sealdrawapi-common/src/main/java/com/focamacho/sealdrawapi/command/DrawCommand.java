package com.focamacho.sealdrawapi.command;

import com.focamacho.sealdrawapi.SealDrawAPI;
import com.focamacho.sealdrawapi.api.Paint;
import com.focamacho.sealdrawapi.api.lib.PaintButton;

import java.util.HashMap;
import java.util.Map;

public abstract class DrawCommand {

    protected final SealDrawAPI api;
    protected final Map<Object, CachedClick> cachedClicks = new HashMap<>();

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

                        if(paint.isPaintBucket()) {
                            CachedClick newClick = new CachedClick(row, column, paint.getDrawing().getColor(row, column), paint.getSelectedColor(player), System.currentTimeMillis());
                            CachedClick oldClick = cachedClicks.put(player, newClick);

                            //Comparar se o pixel clicado é o mesmo do que o clicado anteriormente, e se
                            //não se passaram mais do que 0.2s desde o último click
                            if(oldClick != null && oldClick.getRow() == newClick.getRow() && oldClick.getColumn() == newClick.getColumn() && oldClick.getColor() == newClick.getColor() && (newClick.getTimestamp() - oldClick.getTimestamp()) <= 200) {
                                paint.getDrawing().setColor(row, column, oldClick.getOldColor());
                                paint.fillColor(row, column, newClick.getColor());
                            } else paint.setColor(row, column, paint.getSelectedColor(player));
                        } else paint.setColor(row, column, paint.getSelectedColor(player));

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

    private static class CachedClick {

        private final int row;
        private final int column;
        private final char oldColor;
        private final char color;
        private final long timestamp;

        public CachedClick(int row, int column, char oldColor, char color, long timestamp) {
            this.row = row;
            this.column = column;
            this.oldColor = oldColor;
            this.color = color;
            this.timestamp = timestamp;
        }

        public int getRow() {
            return this.row;
        }

        public int getColumn() {
            return this.column;
        }

        public char getOldColor() {
            return this.oldColor;
        }

        public char getColor() {
            return this.color;
        }

        public long getTimestamp() {
            return this.timestamp;
        }

    }

}
