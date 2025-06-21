package org.eve.i_love_soil.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ILSCapabilities {
    public static final Capability<ISoilCapability> SOIL_CHUNK_DATA_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    public static final Capability<IWindCapability> WIND_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

//    @SubscribeEvent
//    public static void register(RegisterCapabilitiesEvent event) {
//        // Register the capability for IChunkData class
//        event.register(ISoilCapability.class);
//    }
}
