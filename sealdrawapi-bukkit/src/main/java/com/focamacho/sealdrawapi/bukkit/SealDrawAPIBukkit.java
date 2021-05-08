package com.focamacho.sealdrawapi.bukkit;

import com.focamacho.sealdrawapi.SealDrawAPI;
import com.focamacho.sealdrawapi.bukkit.command.DrawCommand;
import com.focamacho.sealdrawapi.bukkit.logger.DrawFilter;
import com.focamacho.sealdrawapi.bukkit.packet.PacketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
public final class SealDrawAPIBukkit {

    public static void onEnable(SealDrawAPI instance) {
        Bukkit.getPluginManager().registerEvents(new DrawCommand(instance), (Plugin) instance.getPlugin());
        ((Logger) LogManager.getRootLogger()).addFilter(new DrawFilter());

        try { Bukkit.getPluginManager().registerEvents(new PacketHandler(instance), (Plugin) instance.getPlugin()); } catch(Exception e) { e.printStackTrace(); }
    }

}
