package org.eve.i_love_soil.common.events;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.eve.i_love_soil.ILoveSoil;

import java.awt.*;

@Mod.EventBusSubscriber(modid = ILoveSoil.MODID)
public class ChunkEvents {
    static int timer = 0;
    @SubscribeEvent
    public static void Servertick(TickEvent.ServerTickEvent event){
        timer ++;
        if (timer < 1000){
            return;
        }
        timer = 0;
        MinecraftServer server = event.getServer();
        ServerLevel level = server.getLevel(ServerLevel.OVERWORLD);

    }

    public static void playertick(TickEvent.PlayerTickEvent event){
        Player player = event.player;
    }
}
