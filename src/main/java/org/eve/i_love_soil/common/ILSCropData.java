package org.eve.i_love_soil.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.teamresourceful.resourcefullib.common.lib.Constants;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import org.eve.i_love_soil.ILoveSoil;
import org.eve.i_love_soil.api.BiomeStats;
import org.eve.i_love_soil.api.CropData;

import java.util.HashMap;
import java.util.Map;

public class ILSCropData extends SimpleJsonResourceReloadListener {
    private static final Map<ResourceKey<Block>, CropData> CROP_DATA_MAP = new HashMap<>();
    //public static final Gson GSON = new GsonBuilder().create();
    public ILSCropData() {
        super(Constants.GSON, "crop_data");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        CROP_DATA_MAP.clear();
        object.forEach((key, value) -> {
            JsonObject json = GsonHelper.convertToJsonObject(value, "crop_data");
            CropData biomeStat = CropData.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(false, ILoveSoil.LOGGER::error);
            CROP_DATA_MAP.put(biomeStat.block(), biomeStat);
        });
    }


    public static Map<ResourceKey<Block>, CropData> cropStats() {
        return CROP_DATA_MAP;
    }
}
