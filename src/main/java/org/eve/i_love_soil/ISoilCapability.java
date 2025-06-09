package org.eve.i_love_soil;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface ISoilCapability  {

    int getWater();

    void addWater(int value);

    void setWater(int value);

    float getpH();

    void changePH(float value);

    void setPH(float value);

    void setNutrients(int value);

    void addNutrients(int value);

    int getNutrients();

}