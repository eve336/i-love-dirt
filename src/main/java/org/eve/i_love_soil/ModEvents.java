package org.eve.i_love_soil;

import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.eve.i_love_soil.common.ILSBiomeData;
import org.eve.i_love_soil.datagen.provider.server.BiomeStatsProvider;

@Mod.EventBusSubscriber(modid = ILoveSoil.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    private static final ResourceLocation CHUNK_DATA_ID = new ResourceLocation(ILoveSoil.MODID, "chunk_data");

    @SubscribeEvent
    public static void attachChunkCapabilities(AttachCapabilitiesEvent<LevelChunk> event) {
        //System.out.println(event.getObject().getPos());
        event.addCapability(CHUNK_DATA_ID, new ICapabilityProvider() {
            final SoilChunkData instance = new SoilChunkData();
            final LazyOptional<ISoilCapability> optional = LazyOptional.of(() -> instance);

            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
                return ILSCapabilities.SOIL_CHUNK_DATA_CAPABILITY.orEmpty(cap, optional);
            }
        });
    }



//    @SubscribeEvent
//    public static void onAddReloadListeners(AddReloadListenerEvent event) {
//        event.addListener(new ILSBiomeData());
//    }


//    @SubscribeEvent
//    public static void onPlayerInteract(PlayerInteractEvent event) {
//        if (event.getLevel() instanceof ServerLevel serverLevel) {
//            BlockPos pos = event.getPos();
//            LevelChunk chunk = serverLevel.getChunkAt(pos);
//
//            // Get the chunk's capability
//            chunk.getCapability(ILSCapabilities.SOIL_CHUNK_DATA_CAPABILITY).ifPresent(data -> {
//                // Set the value (just an example: incrementing the value every time)
//
//                data.addWater(1);
//
//                // Log the new value to make sure it's being set
//                System.out.println("New chunk value at " + chunk.getPos() + ": " + data.getWater());
//            });
//        }
//    }



}
