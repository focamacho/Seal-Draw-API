package com.focamacho.sealdrawapi.bukkit;

import com.focamacho.sealdrawapi.SealDrawAPI;
import com.focamacho.sealdrawapi.bukkit.command.DrawCommand;
import com.focamacho.sealdrawapi.bukkit.logger.DrawFilter;
import com.focamacho.sealdrawapi.bukkit.packet.PacketHandler;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.List;

@SuppressWarnings({"unused", "unchecked"})
public final class SealDrawAPIBukkit {

    public static void onEnable(SealDrawAPI instance) {
        Bukkit.getPluginManager().registerEvents(new DrawCommand(instance), (Plugin) instance.getPlugin());
        ((Logger) LogManager.getRootLogger()).addFilter(new DrawFilter());

        try { Bukkit.getPluginManager().registerEvents(new PacketHandler(instance), (Plugin) instance.getPlugin()); } catch(Exception e) { e.printStackTrace(); }
        try {
            Class<?> configClass = Class.forName("org.spigotmc.SpigotConfig");
            Field spamField = configClass.getField("spamExclusions");
            List<String> spamList = (List<String>) spamField.get(null);
            if(!spamList.contains("/sdwa")) {
                List<String> toSet = Lists.newArrayList(spamList);
                toSet.add("/sdwa");
                spamField.set(null, toSet);
            }
        } catch(Exception ignored) {}
    }

}
