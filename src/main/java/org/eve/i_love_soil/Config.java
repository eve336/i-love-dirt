package org.eve.i_love_soil;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = ILoveSoil.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue WIND_REGION_SIZE = BUILDER.comment("The size of the side length of the wind regions").defineInRange("windRegionSize", 5, 1, 100);

    private static final ForgeConfigSpec.BooleanValue CREATIVE_FLIGHT_WIND = BUILDER.comment("Whether to push players in creative flight with wind").define("creativeFlightWind", false);

    private static final ForgeConfigSpec.DoubleValue PLAYER_WIND_MAGNITUDE = BUILDER.comment("The amount the player is pushed around by wind").defineInRange("playerWindMagnitude", 0.165, 0, 1);

    public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER.comment("What you want the introduction message to be for the magic number").define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER.comment("A list of items to log on common setup.").defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static Set<Item> items;

    public static boolean creativeFlightWind;
    public static double playerWindMagnitude;
    public static int windRegionSize;

    private static boolean validateItemName(final Object obj) {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {

        // convert the list of strings into a set of items
        items = ITEM_STRINGS.get().stream().map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).collect(Collectors.toSet());

        creativeFlightWind = CREATIVE_FLIGHT_WIND.get();
        playerWindMagnitude = PLAYER_WIND_MAGNITUDE.get();
        windRegionSize = WIND_REGION_SIZE.get();
    }
}
