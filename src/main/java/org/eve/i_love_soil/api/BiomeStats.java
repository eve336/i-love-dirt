package org.eve.i_love_soil.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import org.eve.i_love_soil.ILoveSoil;

public record BiomeStats(
        ResourceKey<Biome> biome,
        // int temp?
        int water
) {

    //public static final ResourceKey<Biome> DESERT = ResourceKey.create(Registries.BIOME, new ResourceLocation(ILoveSoil.MODID, "DESERT"));

    public static final Codec<BiomeStats> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registries.BIOME).fieldOf("biome").forGetter(BiomeStats::biome),
            Codec.INT.fieldOf("water").forGetter(BiomeStats::water)
    ).apply(instance, BiomeStats::new));
}
