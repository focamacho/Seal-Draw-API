package com.focamacho.sealdrawapi.api;


import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

public class SpongePaint extends AbstractPaint {

    public SpongePaint(Drawing drawing) {
        super(drawing);

        setOnCancel((player, dw) -> {
            Player p = (Player) player;
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "tellraw " + p.getName() + " \"\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n§cAlgo foi cancelado... Acho que você deveria programar o que\n§cacontece quando alguém cancela o desenho.\"");
            closePaint(p);
        });
        setOnConfirm((player, dw) -> {
            Player p = (Player) player;
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "tellraw " + p.getName() + " \"\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n§aAlgo foi confirmado... Acho que você deveria programar o que\n§aacontece quando alguém confirma o desenho.\"");
            closePaint(p);
        });
    }

    @Override
    public void openPaint(Object player) {
        super.openPaint(player);
        Player p = (Player) player;
        players.put(p, '0');
        Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "tellraw " + p.getName() + " " + getEditorMessage(player));
    }

    @Override
    public void updatePaint() {
        for (Object p : players.keySet()) {
            onUpdate.run(p, this.drawing);
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "tellraw " + ((Player)p).getName() + " " + getEditorMessage(p));
        }
    }


}
