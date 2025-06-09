package org.eve.i_love_soil.datagen.provider.server;

import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import org.eve.i_love_soil.ILoveSoil;
import org.eve.i_love_soil.api.BiomeStats;
import org.eve.i_love_soil.datagen.provider.base.ModCodecProvider;

import java.util.List;
import java.util.function.BiConsumer;

import static net.minecraft.world.level.biome.Biomes.*;

public class BiomeStatsProvider extends ModCodecProvider<BiomeStats> {

    public static final ResourceKey<Registry<BiomeStats>> BIOMES_STATS_REGISTRY = ResourceKey.createRegistryKey(new ResourceLocation(ILoveSoil.MODID, "biome_stats"));

    public BiomeStatsProvider(PackOutput packOutput) {
        super(packOutput, BiomeStats.CODEC, BIOMES_STATS_REGISTRY);
        ILoveSoil.LOGGER.info("BiomeStatsProvider initialized");
    }

    @Override
    protected void build(BiConsumer<ResourceLocation, BiomeStats> consumer) {
        // maybe make it slightly acidic, idk probably just make it whatever for gameplay reasons
        System.out.println(DESERT.location().getPath());
        // deserts
        // water content is soil water content, taiga has a load bc of soil stuff
        List<ResourceKey<Biome>> desertList = List.of(DESERT, BADLANDS, ERODED_BADLANDS);
        desertList.forEach(a -> buildBiomeStats(consumer, a, 500, 7.3f, 0));

        buildBiomeStats(consumer, JUNGLE, 7000, 7.0f, 10);
        buildBiomeStats(consumer, BAMBOO_JUNGLE, 7000, 7.0f, 9);
        buildBiomeStats(consumer, SPARSE_JUNGLE, 6000, 7.0f, 13);

        buildBiomeStats(consumer, PLAINS, 4000, 7.0f, 10);
        buildBiomeStats(consumer, SUNFLOWER_PLAINS, 4000, 7.0f, 10);
        buildBiomeStats(consumer, FOREST, 4000, 7.0f, 15);
        buildBiomeStats(consumer, FLOWER_FOREST, 4000, 7.0f, 10);
        buildBiomeStats(consumer, BIRCH_FOREST, 4000, 7.0f, 15);
        buildBiomeStats(consumer, DARK_FOREST, 4000, 7.0f, 15);
        buildBiomeStats(consumer, CHERRY_GROVE, 4000, 7.0f, 15);
        buildBiomeStats(consumer, RIVER, 5300, 7.0f, 12);

        buildBiomeStats(consumer, TAIGA, 8000, 6.3f, 4);
        buildBiomeStats(consumer, SNOWY_TAIGA, 5000, 6.3f, 2);
        buildBiomeStats(consumer, FROZEN_RIVER, 4500, 6.3f, 2);
        buildBiomeStats(consumer, GROVE, 5000, 6.3f, 2);

        buildBiomeStats(consumer, SAVANNA, 2000, 7.0f, 5);
        buildBiomeStats(consumer, SAVANNA_PLATEAU, 2000, 7.0f, 5);
        buildBiomeStats(consumer, WINDSWEPT_SAVANNA, 2000, 7.0f, 5);
    }

    void buildBiomeStats(BiConsumer<ResourceLocation, BiomeStats> consumer, ResourceKey<Biome> biome, int water, float pH, int nutrients){
        consumer.accept(
                new ResourceLocation(ILoveSoil.MODID, biome.location().getPath()),
                new BiomeStats(
                        biome,
                        water,
                        pH,
                        nutrients
                )
        );
    }

    @Override
    public String getName() {
        return "BiomeStats";
    }
}
