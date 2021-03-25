package com.focamacho.sealdrawapi;

import com.focamacho.sealdrawapi.command.BungeeDrawCommand;
import com.focamacho.sealdrawapi.packet.ChatPacketAdapter;
import de.exceptionflug.protocolize.api.protocol.ProtocolAPI;
import de.exceptionflug.protocolize.api.protocol.Stream;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

@SuppressWarnings("unused")
public final class SealDrawAPIBungee {

    public static SealDrawAPI api;

    public static void onEnable(SealDrawAPI instance) {
        api = instance;
        ProxyServer.getInstance().getPluginManager().registerListener((Plugin) instance.getPlugin(), new BungeeDrawCommand());

        //Protocolize
        ProtocolAPI.getEventManager().registerListener(new ChatPacketAdapter(Stream.DOWNSTREAM));
        ProtocolAPI.getEventManager().registerListener(new ChatPacketAdapter(Stream.UPSTREAM));
    }

}
