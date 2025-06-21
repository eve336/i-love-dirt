package org.eve.i_love_soil.datagen.provider.server;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import org.eve.i_love_soil.ILoveSoil;
import org.eve.i_love_soil.api.BiomeStats;
import org.eve.i_love_soil.api.CropData;
import org.eve.i_love_soil.datagen.provider.base.ModCodecProvider;

import java.util.Optional;
import java.util.function.BiConsumer;

public class CropDataProvider extends ModCodecProvider<CropData> {

    public static final ResourceKey<Registry<CropData>> CROP_DATA_REGISTRY = ResourceKey.createRegistryKey(new ResourceLocation(ILoveSoil.MODID, "crop_data"));

    public CropDataProvider(PackOutput packOutput) {
        super(packOutput, CropData.CODEC, CROP_DATA_REGISTRY);
        ILoveSoil.LOGGER.info("CropDataProvider initialized");
    }

    @Override
    protected void build(BiConsumer<ResourceLocation, CropData> consumer) {
        //level.registryAccess().registry(Registries.BLOCK).get().getResourceKey(Blocks.WHEAT);
        buildCropData(consumer, Blocks.WHEAT, 100, 5000, 5.5f, 6.5f, 100, 10);
    }

    void buildCropData(BiConsumer<ResourceLocation, CropData> consumer, Block block, int minWater, int maxWater, float minPH, float maxPH, int minNutrients, int nutrientUsage){
        Optional<ResourceKey<Block>> blockResourceKey = ForgeRegistries.BLOCKS.getResourceKey(block);
        blockResourceKey.ifPresent(key ->
        consumer.accept(
                new ResourceLocation(ILoveSoil.MODID, key.location().getPath()),
                new CropData(
                        key,
                        minWater,
                        maxWater,
                        minPH,
                        maxPH,
                        minNutrients,
                        nutrientUsage
                )
        )
        );
    }

    @Override
    public String getName() {
        return "CropData";
    }
}
