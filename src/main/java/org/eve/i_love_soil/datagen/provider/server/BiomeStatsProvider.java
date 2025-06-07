package org.eve.i_love_soil.datagen.provider.server;

import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import org.eve.i_love_soil.ILoveSoil;
import org.eve.i_love_soil.api.BiomeStats;
import org.eve.i_love_soil.datagen.provider.base.ModCodecProvider;

import java.util.function.BiConsumer;

public class BiomeStatsProvider extends ModCodecProvider<BiomeStats> {

    public static final ResourceKey<Registry<BiomeStats>> BIOMES_STATS_REGISTRY = ResourceKey.createRegistryKey(new ResourceLocation(ILoveSoil.MODID, "biome_stats"));

    public BiomeStatsProvider(PackOutput packOutput) {
        super(packOutput, BiomeStats.CODEC, BIOMES_STATS_REGISTRY);
        ILoveSoil.LOGGER.info("BiomeStatsProvider initialized");
    }

    @Override
    protected void build(BiConsumer<ResourceLocation, BiomeStats> consumer) {
        consumer.accept(
                new ResourceLocation(ILoveSoil.MODID, "desert"),
                new BiomeStats(
                        Biomes.DESERT,
                        500
                )
        );
    }

    @Override
    public String getName() {
        return "BiomeStats";
    }
}
