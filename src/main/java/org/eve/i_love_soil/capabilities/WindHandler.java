package org.eve.i_love_soil.capabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.eve.i_love_soil.ILSUtil;
import org.eve.i_love_soil.common.wind.WindRegion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.eve.i_love_soil.common.wind.WindRegion.boxSize;

// i still need to implement temperature lol
public class WindHandler implements IWindCapability, ICapabilitySerializable<CompoundTag> {
    final WindHandler instance = this;
    final LazyOptional<IWindCapability> optional = LazyOptional.of(() -> instance);

    Map<Long, Vec3> regionToWindVec = new HashMap<>();

    // might be a good idea to store this somewhere else but this is convenient
    List<WindRegion> loadedRegions = new ArrayList<>();

    // i can probably remove this because of the loading system im using
   // Map<WindRegion, Integer> regionLoadedNumber = new HashMap<>();

    // useful for when you know the coords and want to get the wind, say to apply to a player, or to leaves
    @Override
    public Vec3 getWindAt(BlockPos blockPos) {
        return regionToWindVec.getOrDefault(WindRegion.XZToLong(blockPos.getX() / boxSize, blockPos.getZ() / boxSize), Vec3.ZERO);
    }

    @Override
    public void addLoaded(WindRegion windRegion) {
        loadedRegions.add(windRegion);
        // updateRegion(windRegion)
        //Vec3 vec = new Vec3(Math.random() * 0.1, 0, Math.random() * 0.1);
        // if i stick with this then i need a way to generate a circle
        long regionKey = windRegion.objectToLong();
        // ill probably change this later, i think this will stop it from regenerating on save and quit, so i can test data saving
        if (regionToWindVec.get(regionKey) == null) {
            double magnitude = Math.random() * 0.1;
            //double angle = getAngleMask(windRegion);
            double angle = 2 * Math.random() * Math.PI;
            Vec3 vec3 = new Vec3(magnitude * Math.cos(angle), 0, magnitude * Math.sin(angle));
            regionToWindVec.put(regionKey, vec3);
        }
    }

    @Override
    public void removeLoaded(WindRegion windRegion) {
        loadedRegions.remove(windRegion);
    }

    @Override
    public void removeAllLoaded() {
        loadedRegions = null;
    }

    // this is more so just a test
    // kinda testing the cellular automata stuff, making a mask
    // how do i actually test that this works lmao??
    @Override
    public void updateWind() {
        Map<WindRegion, Double> angleMask = new HashMap<>();
        loadedRegions.forEach(windRegion -> {
            Vec3 vector = regionToWindVec.get(windRegion.objectToLong());
            double angle1;
            // im not sure if the vec will ever be null but whatever
            if (vector == null){
                angle1 = 2 * Math.PI * Math.random();
            }
            else angle1 = ILSUtil.VecToAngleRad(vector);
            //double angle2 = (angle1 + 3 * getAngleMask(windRegion)) / 4;
            double angle2 = getAngleMask(windRegion);
            angleMask.put(windRegion, angle2);
        });
        angleMask.forEach((region, angle) -> {
            long key = region.objectToLong();
            double magnitude = Math.random() * 0.1;
            // cos angle = x = A, sin angle = z = O
            Vec3 vec3 = new Vec3(magnitude * Math.cos(angle), 0, magnitude * Math.sin(angle));
            regionToWindVec.put(key, vec3);
        });
    }

    public double getAngleMask(WindRegion windRegion) {
        int rx = windRegion.rx;
        int rz = windRegion.rz;

        int count = 0;
        double angle = 0;

        // how do i make this only factor in directly adjacent squares lol
        for (int i = rx - 1; i <= rx + 1; i++) {
            for (int j = rz - 1; j <= rz + 1; j++) {
                long key = WindRegion.XZToLong(i, j);
                Vec3 vec = regionToWindVec.get(key);
                if (vec != null
                        && key != windRegion.objectToLong()
                ) {
                    // cos angle = x = A, sin angle = z = O
                    //angle = angle + Math.atan(vec.z / vec.x);
                    angle = angle + ILSUtil.VecToAngleRad(vec);
                    count++;
                }
            }
        }
        // i think i need this line otherwise it will return NaN
        if (count == 0) return 0;
        return angle / count;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return ILSCapabilities.WIND_CAPABILITY.orEmpty(cap, optional);
    }

    // it seems like these 2 work
    // find out when serialise nbt is called

    // serialise nbt is also called when just pausing the game, like pressing escape
    // but only sometimes?

    // seems to just get called on world save, like autosaving, i guess thats fine
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag listTag = new ListTag();
        // what is this fucking syntax??
//        for (Map.Entry<Long, Vec3> entry : regionWind.entrySet()){
//            CompoundTag windTag = new CompoundTag();
//            windTag.putLong("region", entry.getKey());
//            Vec3 vec = entry.getValue();
//            windTag.putDouble("x", vec.x);
//            windTag.putDouble("z", vec.z);
//
//            listTag.add(windTag);
//        }
        loadedRegions.forEach(region -> {
            long key = region.objectToLong();
            Vec3 vec = regionToWindVec.get(key);
            if (vec != null) {
                CompoundTag windTag = new CompoundTag();
                windTag.putLong("region", key);
                windTag.putDouble("x", vec.x);
                windTag.putDouble("z", vec.z);
                listTag.add(windTag);
            }
        });
        tag.put("windList", listTag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ListTag listTag = nbt.getList("windList", ListTag.TAG_COMPOUND);
        List<CompoundTag> toRemove = new ArrayList<>();
        listTag.forEach(baseTag -> {
            CompoundTag windTag = (CompoundTag) baseTag;
            long region = windTag.getLong("region");
            int x = windTag.getInt("x");
            int z = windTag.getInt("z");
            // might add y someday but i dont care for the moment
            Vec3 vec = new Vec3(x, 0, z);
            regionToWindVec.put(region, vec);

            // i wonder if this is a good solution
            toRemove.add(windTag);
        });
        toRemove.forEach(listTag::remove);

    }
}
