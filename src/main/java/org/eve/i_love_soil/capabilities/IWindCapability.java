package org.eve.i_love_soil.capabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import org.eve.i_love_soil.common.wind.WindRegion;

@AutoRegisterCapability
public interface IWindCapability {

    Vec3 getWindAt(BlockPos blockPos);

    void updateWind();

//    void addLoaded(long value);
//
//    void removeLoaded(long value);

    void addLoaded(WindRegion windRegion);

    void removeLoaded(WindRegion windRegion);
}
