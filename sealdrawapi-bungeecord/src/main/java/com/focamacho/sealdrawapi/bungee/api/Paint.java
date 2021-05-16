package com.focamacho.sealdrawapi.bungee.api;

import com.focamacho.sealdrawapi.SealDrawAPI;
import com.focamacho.sealdrawapi.api.AbstractPaint;
import com.focamacho.sealdrawapi.api.Drawing;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

@SuppressWarnings("unused")
public class Paint extends AbstractPaint {

    public Paint(SealDrawAPI api, Drawing drawing) {
        super(api, drawing);

        setButton("cancel", "§c[Cancelar]", "§cClique aqui para cancelar.", (player, paint) -> {
            ProxiedPlayer p = (ProxiedPlayer) player;
            p.sendMessage(ComponentSerializer.parse(" \"\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n§cAlgo foi cancelado... Acho que você deveria programar o que\\n§cacontece quando alguém cancela o desenho.\""));
            closePaint(p);
        });
        setButton("confirm", "§a[Confirmar]", "§aClique aqui para confirmar.", (player, paint) -> {
            ProxiedPlayer p = (ProxiedPlayer) player;
            p.sendMessage(ComponentSerializer.parse(" \"\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n§aAlgo foi confirmado... Acho que você deveria programar o que\\n§aacontece quando alguém confirma o desenho.\""));
            closePaint(p);
        });
        setButton("clean", "§f[Limpar]", "§fClique aqui para limpar.", (player, paint) -> {
            paint.clear();
            paint.updatePaint();
        });
    }

    @Override
    public void updatePaint() {
        for (Object p : players.keySet()) {
            onUpdate.run(p, this);
            ((ProxiedPlayer)p).sendMessage(ComponentSerializer.parse(getEditorMessage(p)));
        }
    }


}
