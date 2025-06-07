package org.eve.i_love_soil;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

public class SoilChunkData implements ISoilCapability, INBTSerializable<CompoundTag> {
    public int water = 0;
    private float pH = 7;

    @Override
    public int getWater() {
        return water;
    }

    @Override
    public void addWater(int value) {
        this.water = this.water + value;
    }

    @Override
    public void setWater(int value) {
        this.water = value;
    }


    @Override
    public float getpH() {
        return pH;
    }

    @Override
    public void changePH(float value) {
        this.pH = pH + value;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("water", water);
        tag.putFloat("pH", pH);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        water = nbt.getInt("water");
        pH = nbt.getFloat("pH");
    }
}
