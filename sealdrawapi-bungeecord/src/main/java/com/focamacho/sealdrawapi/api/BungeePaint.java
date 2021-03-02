package com.focamacho.sealdrawapi.api;


import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

public class BungeePaint extends AbstractPaint {

    public BungeePaint(Drawing drawing) {
        super(drawing);

        setOnCancel((player, dw) -> {
            ProxiedPlayer p = (ProxiedPlayer) player;
            p.sendMessage(ComponentSerializer.parse(" \"\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n§cAlgo foi cancelado... Acho que você deveria programar o que\n§cacontece quando alguém cancela o desenho.\""));
            closePaint(p);
        });
        setOnConfirm((player, dw) -> {
            ProxiedPlayer p = (ProxiedPlayer) player;
            p.sendMessage(ComponentSerializer.parse(" \"\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n§aAlgo foi confirmado... Acho que você deveria programar o que\n§aacontece quando alguém confirma o desenho.\""));
            closePaint(p);
        });
    }

    @Override
    public void openPaint(Object player) {
        super.openPaint(player);
        ProxiedPlayer p = (ProxiedPlayer) player;
        players.put(p, '0');
        p.sendMessage(ComponentSerializer.parse(getEditorMessage(player)));
    }

    @Override
    public void updatePaint() {
        for (Object p : players.keySet()) {
            onUpdate.run(p, this);
            ((ProxiedPlayer)p).sendMessage(ComponentSerializer.parse(getEditorMessage(p)));
        }
    }


}
