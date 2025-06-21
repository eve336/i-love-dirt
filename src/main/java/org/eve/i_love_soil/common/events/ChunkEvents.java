package org.eve.i_love_soil.common.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.eve.i_love_soil.capabilities.ILSCapabilities;
import org.eve.i_love_soil.ILoveSoil;
import org.eve.i_love_soil.common.wind.WindRegion;

import java.util.Optional;

import static org.eve.i_love_soil.common.ILSBiomeData.biomeStats;

@Mod.EventBusSubscriber(modid = ILoveSoil.MODID)
public class ChunkEvents {

    // what to do about biomes on chunk boundaries??
    @SubscribeEvent
    public static void ChunkLoadEventLOLE(ChunkEvent.Load event) {
        // maybe add chunk blending between values
        LevelChunk chunk = (LevelChunk) event.getChunk();
        BlockPos pos = chunk.getPos().getWorldPosition();
        Level level = (Level) event.getLevel();
        Holder<Biome> biome = level.getBiome(pos);
        if (event.isNewChunk()) {
            chunk.getCapability(ILSCapabilities.SOIL_CHUNK_DATA_CAPABILITY).ifPresent(data -> {
                Optional<ResourceKey<Biome>> biomeResourceLocation = biome.unwrapKey();
                biomeResourceLocation.ifPresent(a -> {
                    if (biomeStats().get(a) != null) {
                        var b = biomeStats().get(a);
                        data.setWater(b.water());
                        data.setPH(b.pH());
                        data.setNutrients(b.nutrients());
                        // todo test which one is necessary if either
                        //event.getChunk().setUnsaved(true);
                        chunk.setUnsaved(true);

                    }
                });
            });
        }
    }

    @SubscribeEvent
    public static void ChunkLoadEventWind(ChunkEvent.Load event){
        //System.out.println("chunk start loading");
        LevelChunk chunk = (LevelChunk) event.getChunk();
        //BlockPos pos = chunk.getPos().getWorldPosition();
        Level level = (Level) event.getLevel();
//        int box = WindRegion.getBoxSize();
//        int m = chunk.getPos().x;
//        int n = chunk.getPos().z;
//
//        int worldChunkX = 16 * m;
//        int worldChunkZ = 16 * n;
//
//        int blackX = (int) (Math.ceil( (float) worldChunkX / box) * box);
//        int blackZ = (int) (Math.ceil( (float) worldChunkZ / box) * box);
//
//
//        for (int i = blackX; i >= worldChunkX && i <= worldChunkX + 16; i = i + box) {
//            for (int j = blackZ; j >= worldChunkZ && j <= worldChunkZ + 16; j = j + box) {
//                System.out.println(i + " , " + j);
//                WindRegion windRegion = new WindRegion(i ,j);
//                level.getCapability(ILSCapabilities.WIND_CAPABILITY).ifPresent(data -> {
//                    data.addLoaded(windRegion);
//                });
//            }
//        }

        WindChunkLoading(level, chunk, true);

//        if (blackX > worldChunkX && blackX < worldChunkX + 16
//        && blackZ > worldChunkZ && blackZ < worldChunkZ + 16){
//
//            //blackX = (int) (blackX + box);
//            //blackZ = (int) (blackZ + box);
//        }
        //System.out.println("chunk finished loading");
    }

    @SubscribeEvent
    public static void ChunkUnloadEventWind(ChunkEvent.Unload event){
//        LevelChunk chunk = (LevelChunk) event.getChunk();
//        BlockPos pos = chunk.getPos().getWorldPosition();
//        WindRegion windRegion = WindRegion.fromBlockPos(pos);
//        Level level = (Level) event.getLevel();
//        level.getCapability(ILSCapabilities.WIND_CAPABILITY).ifPresent(data -> {
//            data.removeLoaded(windRegion.toLong());
//        });
        LevelChunk chunk = (LevelChunk) event.getChunk();
        Level level = (Level) event.getLevel();
        WindChunkLoading(level, chunk, false);
    }

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
        level.getCapability(ILSCapabilities.WIND_CAPABILITY).ifPresent(data -> {
            data.updateWind();
        });
    }

    public static void playertick(TickEvent.PlayerTickEvent event){
        Player player = event.player;
    }

    static void WindChunkLoading(Level level, LevelChunk chunk, boolean Loaded){
        int box = WindRegion.getBoxSize();
        int m = chunk.getPos().x;
        int n = chunk.getPos().z;

        int worldChunkX = 16 * m;
        int worldChunkZ = 16 * n;

        int blackX = (int) (Math.ceil( (float) worldChunkX / box) * box);
        int blackZ = (int) (Math.ceil( (float) worldChunkZ / box) * box);

        for (int i = blackX; i >= worldChunkX && i <= worldChunkX + 16; i = i + box) {
            for (int j = blackZ; j >= worldChunkZ && j <= worldChunkZ + 16; j = j + box) {
                //System.out.println(i + " , " + j);
                if (!Loaded) System.out.println("unloaded " + i + "," + j);
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
