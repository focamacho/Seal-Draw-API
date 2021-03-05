package com.focamacho.sealdrawapi;

import com.focamacho.sealdrawapi.command.BukkitDrawCommand;
import com.focamacho.sealdrawapi.logger.BukkitDrawFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
public final class SealDrawAPIBukkit {

    public static SealDrawAPI api;

    public static void onEnable(SealDrawAPI instance) {
        api = instance;
        Bukkit.getPluginManager().registerEvents(new BukkitDrawCommand(), (Plugin) instance.getPlugin());
        ((Logger) LogManager.getRootLogger()).addFilter(new BukkitDrawFilter());
    }

}
