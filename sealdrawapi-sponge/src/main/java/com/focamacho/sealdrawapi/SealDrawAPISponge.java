package com.focamacho.sealdrawapi;

import com.focamacho.sealdrawapi.command.SpongeDrawCommand;
import org.spongepowered.api.Sponge;

public class SealDrawAPISponge {

    public static SealDrawAPI api;

    public static void onEnable(SealDrawAPI instance) {
        api = instance;
        Sponge.getEventManager().registerListeners(instance.getPlugin(), new SpongeDrawCommand());
    }

}
