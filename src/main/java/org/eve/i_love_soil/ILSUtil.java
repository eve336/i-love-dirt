package org.eve.i_love_soil;

import net.minecraft.world.phys.Vec3;

public class ILSUtil {


    public static double VecToAngleRad(Vec3 vec){
        return Math.atan2(vec.z, vec.x);
    }
}
