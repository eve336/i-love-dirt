package org.eve.i_love_soil;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Map;

//@AutoRegisterCapability
public interface ISoilCapability {

    int getWater();

    void addWater(int value);

    void setWater(int value);

    float getpH();

    void changePH(float value);

}
