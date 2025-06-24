package org.eve.i_love_soil.common.wind;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.eve.i_love_soil.Config;
import org.eve.i_love_soil.ILoveSoil;
import org.eve.i_love_soil.capabilities.ILSCapabilities;

import static org.eve.i_love_soil.Config.playerWindMagnitude;

@Mod.EventBusSubscriber(modid = ILoveSoil.MODID)
public class WindEventsHandler {

    static int timer2 = 0;
    @SubscribeEvent
    public static void playertick(TickEvent.PlayerTickEvent event){
        // EUGGHH i neeed to send packets to the client i think
        timer2 ++;
        if (timer2 < 2){
            return;
        }
        timer2 = 0;
        Player player = event.player;
        Level level = player.level();
        if (!level.isClientSide()) return;
        //event.player.addDeltaMovement(new Vec3(0, 1, 0));
        // why does this work???? Why does the level have the
        level.getCapability(ILSCapabilities.WIND_CAPABILITY).ifPresent(data -> {
            Vec3 vec = data.getWindAt(player.blockPosition());
            //System.out.println("player push vec: " + vec);
            // add config
            // add config for min speed, vec multiplier, and whether it pushes you in creative flight
            if (player.getAbilities().flying && !Config.creativeFlightWind) return;
            if (player.getDeltaMovement().length() > 0.1 || !player.onGround()) player.addDeltaMovement(vec.multiply(playerWindMagnitude, 0, playerWindMagnitude));
        });
    }

    @SubscribeEvent
    public static void ChunkLoadEventWind(ChunkEvent.Load event){
        LevelChunk chunk = (LevelChunk) event.getChunk();
        Level level = (Level) event.getLevel();
        WindRegionLoading(level, chunk, true);
    }

    @SubscribeEvent
    public static void ChunkUnloadEventWind(ChunkEvent.Unload event){
        LevelChunk chunk = (LevelChunk) event.getChunk();
        Level level = (Level) event.getLevel();
        WindRegionLoading(level, chunk, false);
    }

    static void WindRegionLoading(Level level, LevelChunk chunk, boolean Loaded){
        int box = WindRegion.boxSize;
        int m = chunk.getPos().x;
        int n = chunk.getPos().z;

        int worldChunkX = 16 * m;
        int worldChunkZ = 16 * n;

        int blackX = (int) (Math.ceil( (float) worldChunkX / box) * box);
        int blackZ = (int) (Math.ceil( (float) worldChunkZ / box) * box);

        // first one is >= so it INCLUDES the left of the chunk, such as including 0
        // second one is just < so it DOESNT include the right of the chunk, so theres no overlap
        for (int i = blackX; i >= worldChunkX && i < worldChunkX + 16; i = i + box) {
            for (int j = blackZ; j >= worldChunkZ && j < worldChunkZ + 16; j = j + box) {
                //System.out.println(i + " , " + j);
                //if (!Loaded) System.out.println("unloaded " + i + "," + j + " at chunk " + m + "," + n);
                WindRegion windRegion = new WindRegion(i ,j);
                level.getCapability(ILSCapabilities.WIND_CAPABILITY).ifPresent(data -> {
                    if (Loaded) {
                        data.addLoaded(windRegion);
                    }
                    else data.removeLoaded(windRegion);
                });
            }
        }
    }
}
