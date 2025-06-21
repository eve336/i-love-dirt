package org.eve.i_love_soil.common.events;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.eve.i_love_soil.capabilities.*;
import org.eve.i_love_soil.ILoveSoil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = ILoveSoil.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    private static final ResourceLocation CHUNK_DATA_ID = new ResourceLocation(ILoveSoil.MODID, "chunk_data");

    private static final ResourceLocation WIND_DATA_ID = new ResourceLocation(ILoveSoil.MODID, "wind_data");

    @SubscribeEvent
    public static void attachChunkCapabilities(AttachCapabilitiesEvent<LevelChunk> event) {
        //System.out.println(event.getObject().getPos());
        event.addCapability(CHUNK_DATA_ID, new ICapabilitySerializable<CompoundTag>() {
            final SoilChunkData instance = new SoilChunkData();
            final LazyOptional<ISoilCapability> optional = LazyOptional.of(() -> instance);

            @Override
            public CompoundTag serializeNBT() {
                return instance.serializeNBT();
            }
            @Override
            public void deserializeNBT(CompoundTag nbt) {
                instance.deserializeNBT(nbt);
            }

            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
                return ILSCapabilities.SOIL_CHUNK_DATA_CAPABILITY.orEmpty(cap, optional);
            }
        });
    }

    @SubscribeEvent
    public static void attackLevelCapabilities(AttachCapabilitiesEvent<Level> event){
        event.addCapability(WIND_DATA_ID, new ICapabilitySerializable<CompoundTag>() {
            final WindHandler instance = new WindHandler();
            final LazyOptional<IWindCapability> optional = LazyOptional.of(() -> instance);

            @Override
            public CompoundTag serializeNBT() {
                return instance.serializeNBT();
            }
            @Override
            public void deserializeNBT(CompoundTag nbt) {
                instance.deserializeNBT(nbt);
            }

            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                return ILSCapabilities.WIND_CAPABILITY.orEmpty(cap, optional);
            }
        });
    }

//    @SubscribeEvent
//    public void onAttachingCapabilities(final AttachCapabilitiesEvent<LevelChunk> event) {
//        //if (!(event.getObject() instanceof EnergyBasedBlockEntity)) return;
//
////        EnergyStorage backend = new EnergyStorage(((EnergyBasedBlockEntity) event.getObject()).capacity);
//        //LazyOptional<IEnergyStorage> optionalStorage = LazyOptional.of(() -> backend);
//        final SoilChunkData backend = new SoilChunkData();
//        final LazyOptional<ISoilCapability> optionalStorage = LazyOptional.of(() -> backend);
//        //Capability<IEnergyStorage> capability = ForgeCapabilities.ENERGY;
//        Capability<ISoilCapability> capability = ILSCapabilities.SOIL_CHUNK_DATA_CAPABILITY;
//
//        ICapabilityProvider provider = new ICapabilitySerializable<CompoundTag>() {
//            @Override
//            public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction direction) {
//                if (cap == capability) {
//                    return optionalStorage.cast();
//                }
//                return LazyOptional.empty();
//            }
//
//            @Override
//            public CompoundTag serializeNBT() {
////                CompoundTag tag = new CompoundTag();
////                tag.putInt("minWater", minWater);
////                tag.putFloat("minPH", minPH);
////                tag.putInt("minNutrients", minNutrients);
////                return tag;
//                return backend.serializeNBT();
//            }
//
//            @Override
//            public void deserializeNBT(CompoundTag nbt) {
////                minWater = nbt.getInt("minWater");
////                minPH = nbt.getFloat("minPH");
////                minNutrients = nbt.getInt("minNutrients");
//                backend.deserializeNBT(nbt);
//            }
//
////            @Override
////            public IntTag serializeNBT() {
////                return backend.serializeNBT();
////            }
////
////            @Override
////            public void deserializeNBT(IntTag tag) {
////                backend.deserializeNBT(tag);
////            }
//        };
//
//        event.addCapability(CHUNK_DATA_ID, provider);

//    }
    // rework to be persistent
//    @SubscribeEvent
//    public static void attachChunkCapabilities(AttachCapabilitiesEvent<LevelChunk> event) {
//        //System.out.println(event.getObject().getPos());
//        event.addCapability(CHUNK_DATA_ID, new ICapabilityProvider() {
//            final SoilChunkData instance = new SoilChunkData();
//            final LazyOptional<ISoilCapability> optional = LazyOptional.of(() -> instance);
//
//            @Override
//            public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
//                return ILSCapabilities.SOIL_CHUNK_DATA_CAPABILITY.orEmpty(cap, optional);
//            }
//        });

//    }


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
