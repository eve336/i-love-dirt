package org.eve.i_love_soil.common.wind;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import static org.eve.i_love_soil.Config.windRegionSize;

public class WindRegion {
    // wind region coords
    public int rx;
    public int rz;

    // todo config
    // small number seems to cause lag?
    public static int boxSize = windRegionSize;

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

    public long objectToLong() {
        // bitwise or
        // returns 1 if either bit is 0 or 1, else 0
        // bitwise and
        // returns 1 if both bits are 1
        // 0xFFFFFFFFL is just hexadecimal for 32 1s in a row
        //return (((long) rx) << 32) | (rz & 0xFFFFFFFFL);

        return (long)rx & 4294967295L | ((long)rz & 4294967295L) << 32;
    }

    // this should make it so im not making a new object every single time i want to make a long for retrieving a region
    public static long XZToLong(int x, int z){
        return (long)x & 4294967295L | ((long)z & 4294967295L) << 32;
    }

    public BlockPos blockPos(){
        return new BlockPos(rx * boxSize, 0, rz * boxSize);
    }

    public int[][] heightmap(Level level){
        return heightmap(level, rx, rz);
    }

    //int[][] meow = new int[][]{ {}, {}, {}, };
    public int[][] heightmap(Level level, int rx, int rz){
        int minX = rx * boxSize;
        int minZ = rz * boxSize;
        int maxX = minX + boxSize;
        int maxZ = minZ + boxSize;
        int[][] heightmap = new int[boxSize][boxSize];
        // not sure if it should be < or <= lol
        for (int i = minX; i <= maxX; i++) {
            for (int j = minZ; j <= maxZ; j++) {
                for (int k = level.getMaxBuildHeight(); k > level.getMinBuildHeight(); k--) {
                    BlockPos blockPos = new BlockPos(i, k, j);
                    if (level.getBlockState(blockPos).getBlock() != Blocks.AIR){
                        heightmap[i - minX][j - minZ] = k;
                    }
                }

            }
        }
        return heightmap;
    }

//    public static int getBoxSize() {
//        return boxSize;
//    }
}
