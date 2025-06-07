package org.eve.i_love_soil.common;

import com.google.gson.*;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.teamresourceful.resourcefullib.common.lib.Constants;
import com.teamresourceful.resourcefullib.common.networking.PacketHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.biome.Biome;
import org.eve.i_love_soil.ILoveSoil;
import org.eve.i_love_soil.api.BiomeStats;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ILSBiomeData extends SimpleJsonResourceReloadListener {
    private static final Map<ResourceKey<Biome>, BiomeStats> BIOME_STATS_MAP = new HashMap<>();
    //public static final Gson GSON = new GsonBuilder().create();
    public ILSBiomeData() {
        super(Constants.GSON, "biome_stats");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        BIOME_STATS_MAP.clear();
        object.forEach((key, value) -> {
            JsonObject json = GsonHelper.convertToJsonObject(value, "biome_stats");
            BiomeStats biomeStat = BiomeStats.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(false, ILoveSoil.LOGGER::error);
            BIOME_STATS_MAP.put(biomeStat.biome(), biomeStat);
        });

//        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
//            ResourceLocation id = entry.getKey();
//            JsonElement element = entry.getValue();
//
//            try {
//                BiomeWater data = GSON.fromJson(element, BiomeWater.class);
//                // Store or use the parsed data
//                System.out.println("Loaded custom data: " + data);
//            } catch (JsonParseException e) {
//                System.err.println("Failed to parse JSON for " + id + ": " + e.getMessage());
//            }
//        }
    }

//    public static void encodePlanets(FriendlyByteBuf buf) {
//        PacketHelper.writeWithYabn(buf, BiomeStats.CODEC.listOf(), biomeStats().values().stream().toList(), true)
//                .get()
//                .mapRight(DataResult.PartialResult::message)
//                .ifRight(ILoveSoil.LOGGER::error);
//    }
//
//    public static Collection<BiomeStats> decodePlanets(FriendlyByteBuf buf) {
//        return PacketHelper.readWithYabn(buf, BiomeStats.CODEC.listOf(), true)
//                .get()
//                .mapRight(DataResult.PartialResult::message)
//                .ifRight(ILoveSoil.LOGGER::error)
//                .left()
//                .orElse(Collections.emptyList());
//    }
//
//
    public static Map<ResourceKey<Biome>, BiomeStats> biomeStats() {
        return BIOME_STATS_MAP;
    }
}
