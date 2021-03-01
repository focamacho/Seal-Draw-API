package com.focamacho.sealdrawapi.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BukkitPaint extends AbstractPaint {

    public BukkitPaint(Drawing drawing) {
        super(drawing);

        setOnCancel((player, dw) -> {
            Player p = (Player) player;
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + p.getName() + " \"\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n§cAlgo foi cancelado... Acho que você deveria programar o que\n§cacontece quando alguém cancela o desenho.\"");
        });
        setOnConfirm((player, dw) -> {
            Player p = (Player) player;
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + p.getName() + " \"\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n§aAlgo foi confirmado... Acho que você deveria programar o que\n§aacontece quando alguém confirma o desenho.\"");
        });
    }

    @Override
    public void openPaint(Object player) {
        super.openPaint(player);
        Player p = (Player) player;
        players.put(p, '0');
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + p.getName() + " " + getEditorMessage(player));
    }

    @Override
    public void updatePaint() {
        for (Object p : players.keySet()) {
            onUpdate.run(p, this.drawing);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + ((Player)p).getName() + " " + getEditorMessage(p));
        }
    }


}
