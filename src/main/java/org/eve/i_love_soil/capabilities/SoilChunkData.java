package org.eve.i_love_soil.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoilChunkData implements ISoilCapability, ICapabilitySerializable<CompoundTag> {
    final SoilChunkData instance = this;
    final LazyOptional<ISoilCapability> optional = LazyOptional.of(() -> instance);
    public int water = 0;
    private float pH = 7;
    private int nutrients = 0;

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
    public void setPH(float value) {
        this.pH = value;
    }

    @Override
    public void setNutrients(int value) {
        this.nutrients = value;
    }

    @Override
    public void addNutrients(int value) {
        this.nutrients = nutrients + value;
        // uh this might get exploited lol
        // idk if i need this but i dont want it to go negative
        if (this.nutrients < 0) this.nutrients = 0;
    }

    @Override
    public int getNutrients() {
        return nutrients;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("water", water);
        tag.putFloat("pH", pH);
        tag.putInt("nutrients", nutrients);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        water = nbt.getInt("water");
        pH = nbt.getFloat("pH");
        nutrients = nbt.getInt("nutrients");
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return ILSCapabilities.SOIL_CHUNK_DATA_CAPABILITY.orEmpty(cap, optional);
    }
}
