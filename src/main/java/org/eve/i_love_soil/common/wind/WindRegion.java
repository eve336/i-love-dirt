package org.eve.i_love_soil.common.wind;

import net.minecraft.core.BlockPos;
import org.openjdk.nashorn.internal.objects.annotations.Getter;

public class WindRegion {
    int rx;
    int rz;

    public static int boxSize = 50;

    public WindRegion(int x, int z) {
        this.rx = x;
        this.rz = z;
    }

    public static WindRegion fromBlockPos(BlockPos pos) {
        // basically dividing by 64?
        //return new WindRegion(pos.getX() >> 6, pos.getZ() >> 6);
        return new WindRegion(pos.getX() / boxSize, pos.getZ() / boxSize);
    }

    public static WindRegion fromXZ(int x, int z) {
        // basically dividing by 64?
        //return new WindRegion(pos.getX() >> 6, pos.getZ() >> 6);
        return new WindRegion(x / boxSize, z / boxSize);
    }

    public long toLong() {
        // bitwise or
        // returns 1 if either bit is 0 or 1, else 0
        // bitwise and
        // returns 1 if both bits are 1
        // 0xFFFFFFFFL is just hexadecimal for 32 1s in a row
        return (((long) rx) << 32) | (rz & 0xFFFFFFFFL);
    }

    public BlockPos blockPos(long value){
        return null;
    }

    public static int getBoxSize() {
        return boxSize;
    }
}
