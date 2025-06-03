package org.eve.i_love_soil.common.data;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class ILSModels {

    public static void createCrossBlockState(DataGenContext<Block, ? extends Block> ctx,
                                             RegistrateBlockstateProvider prov) {
        prov.simpleBlock(ctx.getEntry(), prov.models().cross(ForgeRegistries.BLOCKS.getKey(ctx.getEntry()).getPath(),
                prov.blockTexture(ctx.getEntry())));
    }
}
