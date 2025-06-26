package org.eve.i_love_soil.common.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.eve.i_love_soil.capabilities.ILSCapabilities;
import org.eve.i_love_soil.ILoveSoil;
import org.eve.i_love_soil.capabilities.IWindCapability;
import org.eve.i_love_soil.common.wind.WindRegion;

import java.util.Optional;

import static org.eve.i_love_soil.common.ILSBiomeData.biomeStats;

@Mod.EventBusSubscriber(modid = ILoveSoil.MODID)
public class ChunkEvents {

    // what to do about biomes on chunk boundaries??
    @SubscribeEvent
    public static void SoilChunkLoading(ChunkEvent.Load event) {
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
}
