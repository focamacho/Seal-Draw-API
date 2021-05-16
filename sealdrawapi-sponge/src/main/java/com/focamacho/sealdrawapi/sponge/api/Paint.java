package com.focamacho.sealdrawapi.sponge.api;

import com.focamacho.sealdrawapi.SealDrawAPI;
import com.focamacho.sealdrawapi.api.Drawing;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

@SuppressWarnings("unused")
public class Paint extends com.focamacho.sealdrawapi.api.Paint {

    public Paint(SealDrawAPI api, Drawing drawing) {
        super(api, drawing);

        setButton("cancel", "§c[Cancelar]", "§cClique aqui para cancelar.", (player, paint) -> {
            Player p = (Player) player;
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "tellraw " + p.getName() + " \"\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n§cAlgo foi cancelado... Acho que você deveria programar o que\\n§cacontece quando alguém cancela o desenho.\"");
            closePaint(p);
        });
        setButton("confirm", "§a[Confirmar]", "§aClique aqui para confirmar.", (player, paint) -> {
            Player p = (Player) player;
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "tellraw " + p.getName() + " \"\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n§aAlgo foi confirmado... Acho que você deveria programar o que\\n§aacontece quando alguém confirma o desenho.\"");
            closePaint(p);
        });
    }

    @Override
    public void updatePaint() {
        for (Object p : players.keySet()) {
            onUpdate.run(p, this);
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "tellraw " + ((Player)p).getName() + " " + getEditorMessage(p));
        }
    }


}
