package org.eve.i_love_soil.capabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.eve.i_love_soil.common.wind.WindRegion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WindHandler implements IWindCapability, ICapabilitySerializable<CompoundTag> {
    final WindHandler instance = this;
    final LazyOptional<IWindCapability> optional = LazyOptional.of(() -> instance);

    Map<Long, Vec3> regionWind = new HashMap<>();

    // might be a good idea to store this somewhere else but this is convenient
    List<WindRegion> loadedRegions = new ArrayList<>();

    Map<WindRegion, Integer> regionLoadedNumber = new HashMap<>();

    // useful for when you know the coords and want to get the wind, say to apply to a player, or to leaves
    @Override
    public Vec3 getWindAt(BlockPos blockPos) {
        //WindRegion windRegion = WindRegion.fromBlockPos(blockPos);
        WindRegion windRegion = new WindRegion(blockPos.getX(), blockPos.getZ());
        var longey = windRegion.toLong();
        if (regionWind.get(windRegion.toLong()) == null) {
            System.out.println("long to vec is null");
            return Vec3.ZERO;
        }
        System.out.println("long to vec is NOT null");
        System.out.println(regionWind.get(windRegion.toLong()));
        return regionWind.get(windRegion.toLong());
        //return regionWind.getOrDefault(windRegion.toLong(), Vec3.ZERO);
    }

    @Override
    public void updateWind() {
        loadedRegions.forEach(windRegion -> {
            BlockPos blockPos = windRegion.blockPos();
            int minX = blockPos.getX();
            int minZ = blockPos.getZ();
            int maxX = minX + WindRegion.boxSize;
            int maxZ = minZ + WindRegion.boxSize;
            long regionKey = windRegion.toLong();

            Vec3 vec = new Vec3(Math.random() * 0.1, 0, Math.random() * 0.1);
            regionWind.put(regionKey, vec);
        });
    }

    @Override
    public void addLoaded(WindRegion windRegion) {
        int loadedNum = regionLoadedNumber.getOrDefault(windRegion, 0);
        regionLoadedNumber.put(windRegion, loadedNum + 1);
        if (loadedNum == 0){
            // updateRegion(windRegion)
            loadedRegions.add(windRegion);
            Vec3 vec = new Vec3(Math.random() * 0.1, 0, Math.random() * 0.1);
            long regionKey = windRegion.toLong();
            regionWind.put(regionKey, vec);
        }
    }

    @Override
    public void removeLoaded(WindRegion windRegion) {
        // region with 1 load will go to 0 (nothing loading it)
        // region with 2 loads (eg spanning 2 chunks) will go to 1
        int loadedNum = regionLoadedNumber.getOrDefault(windRegion, 0);
        regionLoadedNumber.put(windRegion, Math.max(0, loadedNum - 1));
        if (loadedNum - 1 == 0){
            loadedRegions.remove(windRegion);
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return ILSCapabilities.WIND_CAPABILITY.orEmpty(cap, optional);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag listTag = new ListTag();
        for (Map.Entry<Long, Vec3> entry : regionWind.entrySet()){
            CompoundTag windTag = new CompoundTag();
            windTag.putLong("region", entry.getKey());
            Vec3 vec = entry.getValue();
            windTag.putDouble("x", vec.x);
            windTag.putDouble("z", vec.z);

            listTag.add(windTag);
        }
        tag.put("windList", listTag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ListTag listTag = nbt.getList("windList", ListTag.TAG_COMPOUND);
        listTag.forEach(baseTag -> {
            CompoundTag windTag = (CompoundTag) baseTag;
            long region = windTag.getLong("region");
            int x = windTag.getInt("x");
            int z = windTag.getInt("z");
            // might add y someday but i dont care for the moment
            Vec3 vec = new Vec3(x, 0, z);
            regionWind.put(region, vec);
        });
    }
}
