package org.eve.i_love_soil.common.events;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.eve.i_love_soil.capabilities.ILSCapabilities;
import org.eve.i_love_soil.ILoveSoil;
import org.eve.i_love_soil.api.CropData;

import java.util.Optional;

import static org.eve.i_love_soil.common.ILSCropData.cropStats;

@Mod.EventBusSubscriber(modid = ILoveSoil.MODID)
public class CropEventHandler {



    @SubscribeEvent
    public static void CropGrowEvent(BlockEvent.CropGrowEvent.Pre event) {
        var level = event.getLevel();
        level.registryAccess().registry(Registries.BLOCK).get().getResourceKey(Blocks.WHEAT);
        var pos = event.getPos();
        var state = event.getState();
        var block = state.getBlock();
        RandomSource random = level.getRandom();
        LevelChunk chunk = (LevelChunk) level.getChunk(pos);
        chunk.getCapability(ILSCapabilities.SOIL_CHUNK_DATA_CAPABILITY).ifPresent(data -> {
//            if (state.getBlock() == Blocks.WHEAT) {
//                if (data.getWater() > 100) {
//                    event.setResult(Event.Result.DENY);
//                    System.out.println("cancelled");
//                } else event.setResult(Event.Result.DEFAULT);
//            }
            Optional<ResourceKey<Block>> optionalKey = ForgeRegistries.BLOCKS.getResourceKey(block);
            optionalKey.ifPresent(realKey -> {
                CropData cropData = cropStats().get(realKey);
                if (cropData == null) return;
                int nutrientUsage = cropData.nutrientUsage();
                int nutrientReq = cropData.minNutrients();
                int nutrients = data.getNutrients();
                if (nutrients < nutrientReq) {event.setResult(Event.Result.DENY); return;}
                if (data.getpH() > cropData.maxPH() || data.getpH() < cropData.minPH()) {event.setResult(Event.Result.DENY); return;}
                int minWater = cropData.minWater();
                int maxWater = cropData.maxWater();
                float water = data.getWater();
                if (water > maxWater || water < minWater) {event.setResult(Event.Result.DENY); return;}

                float b = 0.5f;
                double a = -Math.log(b);
                double multiplier = 0.01f;
                double x = nutrients * multiplier;
                double cropGrowChance = -Math.exp(-x - a) + b;
                //System.out.println(cropGrowChance);
                if (random.nextDouble() < cropGrowChance) {
                    event.setResult(Event.Result.DEFAULT);
                    if (event.getResult() == Event.Result.ALLOW) {
                        data.addNutrients(-nutrientUsage);
                        chunk.setUnsaved(true);
                    }
                }
            }
            );
        });
    }

    // todo either make bonemeal work the same, but not ignore conditions, or make it increase soil minNutrients, or make it increase crop growth speed
    @SubscribeEvent
    public static void BonemealEvent(BonemealEvent event) {
        Block block = event.getBlock().getBlock();
        if (block instanceof CropBlock) {
//            event.setResult(Event.Result.DENY);
            event.setCanceled(true);
        }
    }
}