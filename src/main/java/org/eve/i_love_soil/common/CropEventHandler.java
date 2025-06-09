package org.eve.i_love_soil.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.eve.i_love_soil.ILSCapabilities;
import org.eve.i_love_soil.ILoveSoil;

import java.util.Arrays;
import java.util.Optional;

import static org.eve.i_love_soil.common.ILSBiomeData.biomeStats;

@Mod.EventBusSubscriber(modid = ILoveSoil.MODID)
public class CropEventHandler {

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
                        //chunk.setUnsaved(true);
                        event.getChunk().setUnsaved(true);
                        chunk.setUnsaved(true);

                    }
                });
            });
        }
    }

    @SubscribeEvent
    public static void CropGrowEvent(BlockEvent.CropGrowEvent.Pre event) {
        var level = event.getLevel();
        var pos = event.getPos();
        var state = event.getState();
        var block = state.getBlock();
        LevelChunk chunk = (LevelChunk) level.getChunk(pos);
        chunk.getCapability(ILSCapabilities.SOIL_CHUNK_DATA_CAPABILITY).ifPresent(data -> {
            if (state.getBlock() == Blocks.WHEAT) {
                if (data.getWater() > 100) {
                    event.setResult(Event.Result.DENY);
                    System.out.println("cancelled");
                } else event.setResult(Event.Result.DEFAULT);
            }
        });
    }

    // todo either make bonemeal work the same, but not ignore conditions, or make it increase soil nutrients, or make it increase crop growth speed
    @SubscribeEvent
    public static void BonemealEvent(BonemealEvent event) {
        Block block = event.getBlock().getBlock();
        if (block instanceof CropBlock) {
//            event.setResult(Event.Result.DENY);
            event.setCanceled(true);
        }
    }
}