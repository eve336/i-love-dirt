package org.eve.i_love_soil.common.wind;

import net.minecraft.core.BlockPos;
import org.openjdk.nashorn.internal.objects.annotations.Getter;

public class WindRegion {
    // wind region coords
    int rx;
    int rz;

    // todo config
    // small number seems to cause lag?
    public static int boxSize = 5;

    // input world coords
    public WindRegion(int x, int z) {
        this.rx = x / boxSize;
        this.rz = z / boxSize;
    }

    public static WindRegion fromBlockPos(BlockPos pos) {
        // basically dividing by 64?
        //return new WindRegion(pos.getX() >> 6, pos.getZ() >> 6);
        return new WindRegion(pos.getX(), pos.getZ());
    }

    public long toLong() {
        // bitwise or
        // returns 1 if either bit is 0 or 1, else 0
        // bitwise and
        // returns 1 if both bits are 1
        // 0xFFFFFFFFL is just hexadecimal for 32 1s in a row
        //return (((long) rx) << 32) | (rz & 0xFFFFFFFFL);

        return (long)rx & 4294967295L | ((long)rz & 4294967295L) << 32;
    }

    public BlockPos blockPos(){
        return new BlockPos(rx * boxSize, 0, rz * boxSize);
    }

//    public static int getBoxSize() {
//        return boxSize;
//    }
}
