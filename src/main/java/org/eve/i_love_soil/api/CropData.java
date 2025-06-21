package org.eve.i_love_soil.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public record CropData(
        ResourceKey<Block> block,
        // int temp?
        int minWater,
        int maxWater,
        float minPH,
        float maxPH,
        int minNutrients,
        int nutrientUsage
) {


    public static final Codec<CropData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registries.BLOCK).fieldOf("block").forGetter(CropData::block),
            Codec.INT.fieldOf("minWater").forGetter(CropData::minWater),
            Codec.INT.fieldOf("maxWater").forGetter(CropData::maxWater),
            Codec.FLOAT.fieldOf("minPH").forGetter(CropData::minPH),
            Codec.FLOAT.fieldOf("maxPH").forGetter(CropData::maxPH),
            Codec.INT.fieldOf("minNutrients").forGetter(CropData::minNutrients),
            Codec.INT.fieldOf("nutrientUsage").forGetter(CropData::nutrientUsage)
    ).apply(instance, CropData::new));
}