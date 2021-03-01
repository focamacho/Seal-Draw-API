package com.focamacho.sealdrawapi;

import com.focamacho.sealdrawapi.command.BungeeDrawCommand;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public final class SealDrawAPIBungee {

    public static SealDrawAPI api;

    public static void onEnable(SealDrawAPI instance) {
        api = instance;
        ProxyServer.getInstance().getPluginManager().registerListener((Plugin) instance.getPlugin(), new BungeeDrawCommand());
    }

}
