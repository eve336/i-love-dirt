package org.eve.i_love_soil.mixin;

import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import org.eve.i_love_soil.common.data.VariableHeightGrower;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(Blocks.class)
public class SaplingsMixin {
    @ModifyArg(
            method = "<clinit>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/SaplingBlock;<init>(Lnet/minecraft/world/level/block/grower/AbstractTreeGrower;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V",
                    ordinal = 0
            )
    )
    private static AbstractTreeGrower mixin(AbstractTreeGrower pTreeGrower){
        return new VariableHeightGrower(TreeFeatures.OAK, 5, 15, 5);
    }
//    private static SaplingBlock mixin(AbstractTreeGrower pTreeGrower, BlockBehaviour.Properties pProperties, Operation<SaplingBlock> original){
//        return original.call(new VariableHeightGrower(TreeFeatures.OAK, 5, 15, 5), pProperties);
//    }

//    private static void mixin(){
////        args.set(1, new SaplingBlock(new OakTreeGrower(), BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));
////        //return register("oak_sapling", new SaplingBlock(new VariableHeightGrower(TreeFeatures.OAK, 5, 15, 5), BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));
//
//    }
}
