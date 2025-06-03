package org.eve.i_love_soil;

//@AutoRegisterCapability
public interface ISoilCapability {

    int getWater();

    void addWater(int value);

    float getpH();

    void changePH(float value);
}
