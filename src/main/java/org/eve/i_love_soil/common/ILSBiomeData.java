package org.eve.i_love_soil.common;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.biome.Biome;
import org.eve.i_love_soil.ILoveSoil;
import org.eve.i_love_soil.api.BiomeStats;

import java.util.HashMap;
import java.util.Map;

public class ILSBiomeData extends SimpleJsonResourceReloadListener {
    private static final Map<ResourceKey<Biome>, BiomeStats> BIOME_STATS_MAP = new HashMap<>();
    //public static final Gson GSON = new GsonBuilder().create();
    public ILSBiomeData() {
        super(new Gson(), "biome_stats");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        BIOME_STATS_MAP.clear();
        object.forEach((key, value) -> {
            JsonObject json = GsonHelper.convertToJsonObject(value, "biome_stats");
            BiomeStats biomeStat = BiomeStats.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(false, ILoveSoil.LOGGER::error);
            BIOME_STATS_MAP.put(biomeStat.biome(), biomeStat);
        });
    }


    public static Map<ResourceKey<Biome>, BiomeStats> biomeStats() {
        return BIOME_STATS_MAP;
    }
}
