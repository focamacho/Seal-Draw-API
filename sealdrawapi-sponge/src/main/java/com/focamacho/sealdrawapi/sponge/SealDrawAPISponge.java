package com.focamacho.sealdrawapi.sponge;

import com.focamacho.sealdrawapi.SealDrawAPI;
import com.focamacho.sealdrawapi.sponge.command.DrawCommand;
import com.focamacho.sealdrawapi.sponge.packet.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.api.Sponge;

@SuppressWarnings("unused")
public class SealDrawAPISponge {

    public static void onEnable(SealDrawAPI instance) {
        Sponge.getEventManager().registerListeners(instance.getPlugin(), new DrawCommand(instance));
        MinecraftForge.EVENT_BUS.register(new PacketHandler(instance));
    }

}
