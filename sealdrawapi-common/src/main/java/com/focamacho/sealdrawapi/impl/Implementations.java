package com.focamacho.sealdrawapi.impl;

import com.focamacho.sealdrawapi.SealDrawAPI;
import com.focamacho.sealdrawapi.api.Drawing;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Implementations {

    public static Constructor<?> paintConstructor;

    public static void init(SealDrawAPI instance) {
        Constructor<?> toAssign = null;

        //Definir o construtor que será implementado a partir
        //da detecção de qual Software foi iniciado.
        //Bukkit, Bungee ou Sponge
        try {
            Class.forName("org.bukkit.Bukkit");
            toAssign = Class.forName("com.focamacho.sealdrawapi.bukkit.api.Paint").getDeclaredConstructor(SealDrawAPI.class, Drawing.class);
            Class.forName("com.focamacho.sealdrawapi.bukkit.SealDrawAPIBukkit").getMethod("onEnable", SealDrawAPI.class).invoke(null, instance);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ignore) {
            try {
                Class.forName("net.md_5.bungee.api.ProxyServer");
                toAssign = Class.forName("com.focamacho.sealdrawapi.bungee.api.Paint").getDeclaredConstructor(SealDrawAPI.class, Drawing.class);
                Class.forName("com.focamacho.sealdrawapi.bungee.SealDrawAPIBungee").getMethod("onEnable", SealDrawAPI.class).invoke(null, instance);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
                try {
                    Class.forName("org.spongepowered.api.Sponge");
                    toAssign = Class.forName("com.focamacho.sealdrawapi.sponge.api.Paint").getDeclaredConstructor(SealDrawAPI.class, Drawing.class);
                    Class.forName("com.focamacho.sealdrawapi.sponge.SealDrawAPISponge").getMethod("onEnable", SealDrawAPI.class).invoke(null, instance);
                } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored1) {}
            }
        }

        paintConstructor = toAssign;
    }

}
